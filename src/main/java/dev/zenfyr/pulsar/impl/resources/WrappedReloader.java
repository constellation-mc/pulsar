package dev.zenfyr.pulsar.impl.resources;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

public record WrappedReloader(ResourceLocation location, PreparableReloadListener delegate)
    implements IdentifiableResourceReloadListener {

  @Override
  public ResourceLocation getFabricId() {
    return location();
  }

  @Override
  public CompletableFuture<Void> reload(
      PreparationBarrier preparationBarrier,
      ResourceManager resourceManager,
      ProfilerFiller preparationsProfiler,
      ProfilerFiller reloadProfiler,
      Executor backgroundExecutor,
      Executor gameExecutor) {
    return delegate()
        .reload(
            preparationBarrier,
            resourceManager,
            preparationsProfiler,
            reloadProfiler,
            backgroundExecutor,
            gameExecutor);
  }

  @Override
  public String getName() {
    return delegate().getName();
  }
}
