package dev.zenfyr.pulsar.impl.mixins.resources;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.zenfyr.pulsar.api.resources.ReloaderType;
import dev.zenfyr.pulsar.impl.resources.InternalContentsAccessor;
import dev.zenfyr.pulsar.impl.resources.InternalContext;
import dev.zenfyr.pulsar.impl.resources.WrappedReloader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ReloadableServerResources.class, priority = 1100)
abstract class ReloadableServerResourcesMixin implements InternalContentsAccessor {

  @Unique private final Map<ResourceLocation, PreparableReloadListener> reloadersByIdentifier =
      new HashMap<>();

  @Unique private final IdentityHashMap<ReloaderType<?>, PreparableReloadListener> reloadersByType =
      new IdentityHashMap<>();

  @Override
  public <T extends PreparableReloadListener> T pulsar$getReloader(ReloaderType<T> type) {
    PreparableReloadListener reloader = this.reloadersByType.get(type);
    if (reloader == null) {
      synchronized (this.reloadersByIdentifier) {
        reloader = this.reloadersByIdentifier.get(type.location());
        if (reloader == null)
          throw new NoSuchElementException("Missing reloader %s".formatted(type.location()));
        this.reloadersByType.put(type, reloader);
      }
    }
    if (reloader instanceof WrappedReloader wrapped) {
      reloader = wrapped.delegate();
    }
    return (T) reloader;
  }

  @Override
  public void pulsar$setReloaders(List<IdentifiableResourceReloadListener> reloaders) {
    this.reloadersByIdentifier.clear();
    this.reloadersByType.clear();

    for (IdentifiableResourceReloadListener reloader : reloaders) {
      this.reloadersByIdentifier.put(reloader.getFabricId(), reloader);
    }
  }

  @WrapOperation(
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/server/packs/resources/SimpleReloadInstance;create(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/server/packs/resources/ReloadInstance;"),
      method = "loadResources")
  private static ReloadInstance setContext(
      ResourceManager manager,
      List<PreparableReloadListener> reloaders,
      Executor prepareExecutor,
      Executor applyExecutor,
      CompletableFuture<Unit> initialStage,
      boolean profiled,
      Operation<ReloadInstance> original,
      @Local ReloadableServerResources contents,
      @Local(argsOnly = true) RegistryAccess.Frozen registryManager,
      @Local(argsOnly = true) FeatureFlagSet featureSet) {
    try {
      InternalContext.LOCAL.set(new InternalContext(registryManager, featureSet, contents));
      return original.call(
          manager, reloaders, prepareExecutor, applyExecutor, initialStage, profiled);
    } finally {
      InternalContext.LOCAL.remove();
    }
  }
}
