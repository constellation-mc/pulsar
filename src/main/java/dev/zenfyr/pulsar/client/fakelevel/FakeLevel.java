package dev.zenfyr.pulsar.client.fakelevel;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Lifecycle;
import dev.zenfyr.pulsar.client.events.AfterFirstReload;
import dev.zenfyr.pulsar.util.Utilities;
import java.util.*;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.telemetry.TelemetryEventSender;
import net.minecraft.client.telemetry.WorldSessionTelemetryManager;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ServerLinks;
import net.minecraft.world.Difficulty;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import org.jetbrains.annotations.ApiStatus;

/**
 * A fake {@link ClientLevel}, mainly to be used for rendering in GUIs.
 * this instance provides basic {@link RegistryAccess} with built-in worldgen registries bootstrapped.
 */
@UtilityClass
public class FakeLevel {

  private static final ThreadLocal<Boolean> LOADING = ThreadLocal.withInitial(() -> false);

  public static final Supplier<ClientLevel> INSTANCE = Suppliers.memoize(() -> {
    try {
      LOADING.set(true);
      var regs = FakeLevel.getRegistries();

      var immutable = ClientRegistryLayer.createRegistryAccess()
          .replaceFrom(
              ClientRegistryLayer.REMOTE,
              new RegistryAccess.ImmutableRegistryAccess(
                      RegistrySynchronization.networkedRegistries(regs))
                  .freeze());

      ClientPacketListener networkHandler = new ClientPacketListener(
          Minecraft.getInstance(),
          new Connection(PacketFlow.CLIENTBOUND),
          new CommonListenerCookie(
              new LevelLoadTracker(),
              new GameProfile(UUID.randomUUID(), "fake_profile_ratio"),
              new WorldSessionTelemetryManager(TelemetryEventSender.DISABLED, false, null, null),
              immutable.compositeAccess(),
              FeatureFlagSet.of(),
              null,
              null,
              null,
              Map.of(),
              null,
              Map.of(),
              ServerLinks.EMPTY,
              Map.of(),
              false));

      var dimensionTypeRegistry = regs.getAccessForLoading(RegistryLayer.DIMENSIONS)
          .lookupOrThrow(Registries.DIMENSION_TYPE);
      var overworld = dimensionTypeRegistry.getValue(BuiltinDimensionTypes.OVERWORLD);
      return new ClientLevel(
          networkHandler,
          new ClientLevel.ClientLevelData(Difficulty.EASY, false, false),
          Level.OVERWORLD,
          dimensionTypeRegistry.wrapAsHolder(overworld),
          0,
          0,
          Minecraft.getInstance().levelRenderer,
          true,
          0,
          63);
    } finally {
      LOADING.remove();
    }
  });

  /**
   * Returns if the current loading world is the fake {@link ClientLevel}.
   * @return if the current loading world is the fake {@link ClientLevel}.
   */
  public static boolean isLoading() {
    return LOADING.get();
  }

  private static LayeredRegistryAccess<RegistryLayer> getRegistries() {
    LayeredRegistryAccess<RegistryLayer> combinedDynamicRegistries =
        RegistryLayer.createRegistryAccess();
    LayeredRegistryAccess<RegistryLayer> cdr2 = bootstrapBuiltin(combinedDynamicRegistries);

    RegistryAccess.Frozen immutable = cdr2.getAccessForLoading(RegistryLayer.DIMENSIONS);

    WorldDimensions preset = WorldPresets.createNormalWorldDimensions(immutable);
    WorldDimensions.Complete dimensionsConfig =
        preset.bake(new MappedRegistry<>(Registries.LEVEL_STEM, Lifecycle.stable()));

    return cdr2.replaceFrom(RegistryLayer.DIMENSIONS, dimensionsConfig.dimensionsRegistryAccess());
  }

  private static LayeredRegistryAccess<RegistryLayer> bootstrapBuiltin(
      LayeredRegistryAccess<RegistryLayer> cdr) {
    HolderLookup.Provider pain = VanillaRegistries.createLookup();

    List<? extends ResourceKey<? extends Registry<?>>> keys =
        RegistryDataLoader.WORLDGEN_REGISTRIES.stream()
            .map(RegistryDataLoader.RegistryData::key)
            .toList();

    List<MappedRegistry<Object>> regs = new ArrayList<>(keys.size());
    for (ResourceKey<? extends Registry<?>> key : keys) {
      MappedRegistry<Object> registry =
          new MappedRegistry<>(Utilities.cast(key), Lifecycle.stable());

      pain.lookup(key).ifPresent(impl -> impl.listElements()
          .forEach(ref -> registry.register(
              ref.key(), ref.value(), new RegistrationInfo(Optional.empty(), Lifecycle.stable()))));

      registry.freeze();
      regs.add(registry);
    }
    RegistryAccess.Frozen immutable1 =
        new RegistryAccess.ImmutableRegistryAccess(ImmutableList.copyOf(regs)).freeze();
    return cdr.replaceFrom(RegistryLayer.WORLDGEN, immutable1);
  }

  @ApiStatus.Internal
  public static void init() {
    AfterFirstReload.EVENT.register(INSTANCE::get);
  }
}
