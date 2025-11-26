package dev.zenfyr.pulsar.resources;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface DataPackContentsAccessor {

  static DataPackContentsAccessor get(MinecraftServer server) {
      return ((DataPackContentsAccessor) server);
  }

  <T extends PreparableReloadListener> T pulsar$getReloader(ReloaderType<T> type);
}
