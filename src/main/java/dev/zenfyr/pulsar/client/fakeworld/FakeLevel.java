package dev.zenfyr.pulsar.client.fakeworld;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Lifecycle;
import dev.zenfyr.pulsar.client.events.AfterFirstReload;
import dev.zenfyr.pulsar.util.Utilities;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ClientRegistryLayer;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import org.jetbrains.annotations.ApiStatus;

/**
 * A fake {@link ClientLevel}, mainly to be used for rendering in GUIs
 */
@UtilityClass
public class FakeLevel {

  private static final ThreadLocal<Boolean> LOADING = ThreadLocal.withInitial(() -> false);

  public static final Supplier<ClientLevel> INSTANCE = Suppliers.memoize(() -> {
    try {
      LOADING.set(true);
      var regs = FakeLevel.getRegistries();

      ClientPacketListener networkHandler = new ClientPacketListener(
          Minecraft.getInstance(),
          null,
          new Connection(PacketFlow.CLIENTBOUND),
          null,
          new GameProfile(UUID.randomUUID(), "fake_profile_ratio"),
          null);
      networkHandler.registryAccess = ClientRegistryLayer.createRegistryAccess()
          .replaceFrom(
              ClientRegistryLayer.REMOTE,
              new RegistryAccess.ImmutableRegistryAccess(
                      RegistrySynchronization.networkedRegistries(regs))
                  .freeze());

      return new ClientLevel(
          networkHandler,
          new ClientLevel.ClientLevelData(Difficulty.EASY, false, false),
          Level.OVERWORLD,
          regs.getAccessForLoading(RegistryLayer.DIMENSIONS)
              .registryOrThrow(Registries.DIMENSION_TYPE)
              .getHolderOrThrow(BuiltinDimensionTypes.OVERWORLD),
          0,
          0,
          null,
          Minecraft.getInstance().levelRenderer,
          true,
          0);
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
          .forEach(ref -> registry.register(ref.key(), ref.value(), Lifecycle.stable())));

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
