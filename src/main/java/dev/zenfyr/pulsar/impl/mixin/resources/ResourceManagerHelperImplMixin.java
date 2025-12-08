package dev.zenfyr.pulsar.impl.mixin.resources;

import com.llamalad7.mixinextras.sugar.Local;
import dev.zenfyr.pulsar.resources.ServerReloadersEvent;
import dev.zenfyr.pulsar.resources.impl.ContextImpl;
import dev.zenfyr.pulsar.resources.impl.InternalContentsAccessor;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.fabricmc.fabric.impl.resource.v1.DataResourceLoaderImpl;
import net.fabricmc.fabric.impl.resource.v1.SetupMarkerResourceReloader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DataResourceLoaderImpl.class, priority = 1100)
public class ResourceManagerHelperImplMixin {

  @Inject(
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/fabricmc/fabric/impl/resource/v1/ResourceLoaderImpl;collectReloadersToAdd(Lnet/fabricmc/fabric/impl/resource/v1/SetupMarkerResourceReloader;)Ljava/util/Set;",
              shift = At.Shift.BY,
              by = 2),
      method = "collectReloadersToAdd",
      remap = false)
  private void injectReloaders(
      @Nullable SetupMarkerResourceReloader marker,
      CallbackInfoReturnable<Set<Map.Entry<ResourceLocation, PreparableReloadListener>>> cir,
      @Local Set<Map.Entry<ResourceLocation, PreparableReloadListener>> reloadersToAdd) {
    ContextImpl context = new ContextImpl(
        marker.registries(),
        marker.featureSet(),
        listener -> reloadersToAdd.add(Map.entry(listener.getFabricId(), listener)),
        type -> ((InternalContentsAccessor) marker.dataPackContents()).pulsar$getReloader(type));
    ServerReloadersEvent.EVENT.invoker().onServerReloaders(context);
  }

  @Inject(at = @At("TAIL"), method = "collectReloadersToAdd", remap = false)
  private void setEventResults(
      @Nullable SetupMarkerResourceReloader setupMarker,
      CallbackInfoReturnable<Set<Map.Entry<ResourceLocation, PreparableReloadListener>>> cir,
      @Local Set<Map.Entry<ResourceLocation, PreparableReloadListener>> reloadersToAdd) {
    ((InternalContentsAccessor) setupMarker.dataPackContents())
        .pulsar$setReloaders(reloadersToAdd.stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }
}
