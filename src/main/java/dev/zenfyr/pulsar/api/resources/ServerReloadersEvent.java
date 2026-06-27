package dev.zenfyr.pulsar.api.resources;

import dev.zenfyr.pulsar.api.event.Bus;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.flag.FeatureFlagSet;

/**
 * Allows registering new data pack reloaders with proper context. This supports proper managers akin to the RecipeManager and LootManager.
 * <p>
 * The Fabric API is side-agnostic, so you have to rely on static hooks. It also lacks a way to retrieve {@link RegistryAccess}.
 * </p>
 */
public interface ServerReloadersEvent {

  Bus<ServerReloadersEvent> EVENT = Bus.create(ServerReloadersEvent.class, events -> (c) -> {
    for (ServerReloadersEvent event : events) {
      event.onServerReloaders(c);
    }
  });

  void onServerReloaders(Context context);

  interface Context {
    RegistryAccess registryAccess();

    FeatureFlagSet featureFlags();

    void register(IdentifiableResourceReloadListener reloadListener);

    void register(ResourceLocation location, PreparableReloadListener reloadListener);

    /**
     * Returns a reloader by type. <br/>
     * Due to a design oversight, calling this method during the event will crash the game. Using it during {@code prepare} and {@code apply} is fine.
     */
    <T extends PreparableReloadListener> T reloader(ReloaderType<T> type);
  }
}
