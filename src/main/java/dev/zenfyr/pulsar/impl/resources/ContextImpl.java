package dev.zenfyr.pulsar.impl.resources;

import dev.zenfyr.pulsar.api.resources.ReloadListenerType;
import dev.zenfyr.pulsar.api.resources.ServerReloadListenersEvent;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.flag.FeatureFlagSet;

public record ContextImpl(
    HolderLookup.Provider registryAccess,
    FeatureFlagSet featureFlags,
    BiConsumer<ResourceLocation, PreparableReloadListener> registrar,
    Function<ReloadListenerType<?>, PreparableReloadListener> provider)
    implements ServerReloadListenersEvent.Context {

  @Override
  public void register(ResourceLocation location, PreparableReloadListener reloadListener) {
    registrar().accept(location, reloadListener);
  }

  @Override
  public <T extends PreparableReloadListener> T getListener(ReloadListenerType<T> type) {
    return (T) provider().apply(type);
  }
}
