package dev.zenfyr.pulsar.impl.creativetab;

import dev.zenfyr.pulsar.api.creativetab.CreativeModeTabAnimaton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.CreativeModeTab;

@Environment(EnvType.CLIENT)
public interface CreativeModeTabExtensions {

  default CreativeModeTab pulsar$setIconAnimation(CreativeModeTabAnimaton animation) {
    throw new IllegalStateException("Interface not implemented");
  }

  default CreativeModeTabAnimaton pulsar$getIconAnimation() {
    throw new IllegalStateException("Interface not implemented");
  }
}
