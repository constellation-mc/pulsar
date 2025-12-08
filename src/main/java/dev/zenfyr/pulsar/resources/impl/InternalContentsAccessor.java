package dev.zenfyr.pulsar.resources.impl;

import dev.zenfyr.pulsar.resources.DataPackContentsAccessor;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface InternalContentsAccessor extends DataPackContentsAccessor {
  void pulsar$setReloaders(Map<ResourceLocation, PreparableReloadListener> reloaders);
}
