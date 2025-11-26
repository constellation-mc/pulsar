package dev.zenfyr.pulsar.resources.impl;

import dev.zenfyr.pulsar.resources.DataPackContentsAccessor;
import java.util.List;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;

public interface InternalContentsAccessor extends DataPackContentsAccessor {
  void pulsar$setReloaders(List<IdentifiableResourceReloadListener> reloaders);
}
