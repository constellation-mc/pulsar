package dev.zenfyr.pulsar.resources;

import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface DataPackContentsAccessor {

  <T extends PreparableReloadListener> T dm$getReloader(ReloaderType<T> type);
}
