package dev.zenfyr.pulsar.impl.resources;

import dev.zenfyr.pulsar.api.resources.ReloaderType;
import dev.zenfyr.pulsar.api.resources.ServerReloadersEvent;
import java.util.function.BiConsumer;
import java.util.function.Function;
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

  @Override
  public void register(Identifier identifier, PreparableReloadListener reloadListener) {
    registrar().accept(identifier, reloadListener);
  }

  @Override
  public <T extends PreparableReloadListener> T reloader(ReloaderType<T> type) {
    return (T) provider().apply(type);
  }
}
