package dev.zenfyr.pulsar.impl.mixin.resources;

import dev.zenfyr.pulsar.resources.DataPackContentsAccessor;
import dev.zenfyr.pulsar.resources.ReloaderType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftServer.class)
abstract class MinecraftServerMixin implements DataPackContentsAccessor {

  @Shadow
  private MinecraftServer.ReloadableResources resources;

  @Override
  public <T extends PreparableReloadListener> T dm$getReloader(ReloaderType<T> type) {
    return ((DataPackContentsAccessor) resources.managers()).dm$getReloader(type);
  }
}
