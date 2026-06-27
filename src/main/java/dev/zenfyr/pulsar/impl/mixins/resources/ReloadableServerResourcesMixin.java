package dev.zenfyr.pulsar.impl.mixins.resources;

import dev.zenfyr.pulsar.api.resources.ReloadListenerType;
import dev.zenfyr.pulsar.impl.resources.InternalContentsAccessor;
import java.util.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = ReloadableServerResources.class, priority = 1100)
abstract class ReloadableServerResourcesMixin implements InternalContentsAccessor {

  @Unique private final Map<ResourceLocation, PreparableReloadListener> reloadersByIdentifier =
      new HashMap<>();

  @Unique private final IdentityHashMap<ReloadListenerType<?>, PreparableReloadListener> reloadersByType =
      new IdentityHashMap<>();

  @Override
  public <T extends PreparableReloadListener> T pulsar$getReloadListener(
      ReloadListenerType<T> type) {
    var reloader = this.reloadersByType.get(type);
    if (reloader == null) {
      synchronized (this.reloadersByIdentifier) {
        reloader = this.reloadersByIdentifier.get(type.location());
        if (reloader == null)
          throw new NoSuchElementException("Missing reloader %s".formatted(type.location()));
        this.reloadersByType.put(type, reloader);
      }
    }
    return (T) reloader;
  }

  @Override
  public void pulsar$setReloaders(Map<ResourceLocation, PreparableReloadListener> reloaders) {
    this.reloadersByIdentifier.clear();
    this.reloadersByType.clear();

    this.reloadersByIdentifier.putAll(reloaders);
  }
}
