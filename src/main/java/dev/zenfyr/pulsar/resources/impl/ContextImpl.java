package dev.zenfyr.pulsar.resources.impl;

import dev.zenfyr.pulsar.resources.ReloaderType;
import dev.zenfyr.pulsar.resources.ServerReloadersEvent;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.flag.FeatureFlagSet;

public record ContextImpl(
    HolderLookup.Provider registryAccess,
    FeatureFlagSet featureFlags,
    BiConsumer<Identifier, PreparableReloadListener> registrar,
    Function<ReloaderType<?>, PreparableReloadListener> provider)
    implements ServerReloadersEvent.Context {

  @Deprecated
  public void register(IdentifiableResourceReloadListener listener) {
    registrar().accept(listener.getFabricId(), listener);
  }

  @Override
  public void register(Identifier identifier, PreparableReloadListener reloadListener) {
    registrar().accept(identifier, reloadListener);
  }

  @Override
  public <T extends PreparableReloadListener> T reloader(ReloaderType<T> type) {
    return (T) provider().apply(type);
  }
}
