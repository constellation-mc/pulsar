package dev.zenfyr.pulsar.resources.impl;

import dev.zenfyr.pulsar.resources.ReloaderType;
import dev.zenfyr.pulsar.resources.ServerReloadersEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.flag.FeatureFlagSet;

public record ContextImpl(
    RegistryAccess.Frozen registryAccess,
    FeatureFlagSet featureFlags,
    Consumer<IdentifiableResourceReloadListener> registrar,
    Function<ReloaderType<?>, PreparableReloadListener> provider)
    implements ServerReloadersEvent.Context {

  public void register(IdentifiableResourceReloadListener listener) {
    registrar().accept(listener);
  }

  @Override
  public void register(ResourceLocation location, PreparableReloadListener reloadListener) {
    registrar().accept(new IdentifiableResourceReloadListener() {
      @Override
      public ResourceLocation getFabricId() {
        return location;
      }

      @Override
      public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
        return reloadListener.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
      }

      @Override
      public String getName() {
        return reloadListener.getName();
      }
    });
  }

  @Override
  public <T extends PreparableReloadListener> T reloader(ReloaderType<T> type) {
    return (T) provider().apply(type);
  }
}
