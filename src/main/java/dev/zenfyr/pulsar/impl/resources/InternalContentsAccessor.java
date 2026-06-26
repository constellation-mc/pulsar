package dev.zenfyr.pulsar.impl.resources;

import dev.zenfyr.pulsar.api.resources.DataPackContentsAccessor;
import java.util.List;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;

public interface InternalContentsAccessor extends DataPackContentsAccessor {
  void pulsar$setReloaders(List<IdentifiableResourceReloadListener> reloaders);
}
