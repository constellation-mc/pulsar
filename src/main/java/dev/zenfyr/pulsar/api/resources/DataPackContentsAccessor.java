package dev.zenfyr.pulsar.api.resources;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface DataPackContentsAccessor {

  static DataPackContentsAccessor get(MinecraftServer server) {
    return server;
  }

  <T extends PreparableReloadListener> T pulsar$getReloader(ReloaderType<T> type);
}
