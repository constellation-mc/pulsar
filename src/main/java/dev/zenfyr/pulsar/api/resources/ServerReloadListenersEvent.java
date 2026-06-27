package dev.zenfyr.pulsar.api.resources;

import dev.zenfyr.pulsar.api.event.Bus;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.flag.FeatureFlagSet;

/**
 * Allows registering new data pack reloaders with proper context. This supports proper managers akin to the RecipeManager and LootManager.
 * <p>
 * The Fabric API is side-agnostic, so you have to rely on static hooks. It also lacks a way to retrieve {@link RegistryAccess}.
 * </p>
 */
public interface ServerReloadListenersEvent {

  Bus<ServerReloadListenersEvent> EVENT =
      Bus.create(ServerReloadListenersEvent.class, events -> (c) -> {
        for (ServerReloadListenersEvent event : events) {
          event.onServerReload(c);
        }
      });

  void onServerReload(Context context);

  interface Context {
    HolderLookup.Provider registryAccess();

    FeatureFlagSet featureFlags();

    void register(Identifier identifier, PreparableReloadListener reloadListener);

    /**
     * Returns a reloader by type. <br/>
     * Due to a design oversight, calling this method during the event will crash the game. Using it during {@code prepare} and {@code apply} is fine.
     */
    <T extends PreparableReloadListener> T getListener(ReloadListenerType<T> type);
  }
}
