package dev.zenfyr.pulsar.impl.mixin.resources;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.zenfyr.pulsar.resources.ServerReloadersEvent;
import dev.zenfyr.pulsar.resources.impl.ContextImpl;
import dev.zenfyr.pulsar.resources.impl.InternalContentsAccessor;
import dev.zenfyr.pulsar.resources.impl.InternalContext;
import java.util.List;
import java.util.Set;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourceManagerHelperImpl.class)
public class ResourceManagerHelperImplMixin {

  @ModifyExpressionValue(
      at = @At(value = "CONSTANT", args = "intValue=-1"),
      method = "sort(Ljava/util/List;)V",
      remap = false)
  private int executeEvent(
      int lastSize,
      @Local(argsOnly = true) List<PreparableReloadListener> reloaders,
      @Local(ordinal = 1) List<IdentifiableResourceReloadListener> toAdd,
      @Local Set<ResourceLocation> resolvedIds) {
    if (InternalContext.LOCAL.get() != null) {
      var internal = InternalContext.LOCAL.get();
      ContextImpl context = new ContextImpl(
          internal.manager(),
          internal.featureSet(),
          listener -> {
            if (!resolvedIds.add(listener.getFabricId())) {
              throw new IllegalStateException(
                  "Tried to register a reloader (%s) twice!".formatted(listener.getFabricId()));
            }
            toAdd.add(listener);
          },
          reloaderType ->
              ((InternalContentsAccessor) internal.contents()).pulsar$getReloader(reloaderType));

      ServerReloadersEvent.EVENT.invoker().onServerReloaders(context);
    }
    return lastSize;
  }

  @Inject(at = @At("TAIL"), method = "sort(Ljava/util/List;)V", remap = false)
  private void setEventResults(List<PreparableReloadListener> listeners, CallbackInfo ci) {
    if (InternalContext.LOCAL.get() != null) {
      var internal = InternalContext.LOCAL.get();
      var cls = IdentifiableResourceReloadListener.class;
      ((InternalContentsAccessor) internal.contents())
          .pulsar$setReloaders(
              listeners.stream().filter(cls::isInstance).map(cls::cast).toList());
    }
  }
}
