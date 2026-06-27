package dev.zenfyr.pulsar.impl.resources;

import dev.zenfyr.pulsar.api.resources.ReloadListenerType;
import dev.zenfyr.pulsar.api.resources.ServerReloadListenersEvent;
import java.util.function.Consumer;
import java.util.function.Function;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.flag.FeatureFlagSet;

public record ContextImpl(
    RegistryAccess registryAccess,
    FeatureFlagSet featureFlags,
    Consumer<IdentifiableResourceReloadListener> registrar,
    Function<ReloadListenerType<?>, PreparableReloadListener> provider)
    implements ServerReloadListenersEvent.Context {

  @Override
  public void register(ResourceLocation location, PreparableReloadListener reloadListener) {
    if (reloadListener instanceof IdentifiableResourceReloadListener identified) {
      registrar().accept(identified);
      return;
    }

    registrar().accept(new WrappedReloader(location, reloadListener));
  }

  @Override
  public <T extends PreparableReloadListener> T getListener(ReloadListenerType<T> type) {
    return (T) provider().apply(type);
  }
}
