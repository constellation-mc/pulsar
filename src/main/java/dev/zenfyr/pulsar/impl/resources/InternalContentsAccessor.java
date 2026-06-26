package dev.zenfyr.pulsar.impl.resources;

import dev.zenfyr.pulsar.api.resources.DataPackContentsAccessor;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface InternalContentsAccessor extends DataPackContentsAccessor {
  void pulsar$setReloaders(Map<ResourceLocation, PreparableReloadListener> reloaders);
}
