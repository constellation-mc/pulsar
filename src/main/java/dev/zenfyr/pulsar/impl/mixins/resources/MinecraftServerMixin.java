package dev.zenfyr.pulsar.impl.mixins.resources;

import dev.zenfyr.pulsar.api.resources.DataPackContentsAccessor;
import dev.zenfyr.pulsar.api.resources.ReloadListenerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftServer.class)
abstract class MinecraftServerMixin implements DataPackContentsAccessor {

  @Shadow
  private MinecraftServer.ReloadableResources resources;

  @Override
  public <T extends PreparableReloadListener> T pulsar$getReloadListener(
      ReloadListenerType<T> type) {
    return ((DataPackContentsAccessor) resources.managers()).pulsar$getReloadListener(type);
  }
}
