package dev.zenfyr.pulsar.resources.impl;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.world.flag.FeatureFlagSet;

public record InternalContext(
    RegistryAccess.Frozen manager, FeatureFlagSet featureSet, ReloadableServerResources contents) {
  public static final ThreadLocal<InternalContext> LOCAL = ThreadLocal.withInitial(() -> null);
}
