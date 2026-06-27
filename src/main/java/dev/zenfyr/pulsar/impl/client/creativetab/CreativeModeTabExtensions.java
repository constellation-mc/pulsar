package dev.zenfyr.pulsar.impl.client.creativetab;

import dev.zenfyr.pulsar.api.client.creativetab.CreativeModeTabAnimation;
import net.minecraft.world.item.CreativeModeTab;

public interface CreativeModeTabExtensions {

  default CreativeModeTab pulsar$setIconAnimation(CreativeModeTabAnimation animation) {
    throw new IllegalStateException("Interface not implemented");
  }

  default CreativeModeTabAnimation pulsar$getIconAnimation() {
    throw new IllegalStateException("Interface not implemented");
  }
}
