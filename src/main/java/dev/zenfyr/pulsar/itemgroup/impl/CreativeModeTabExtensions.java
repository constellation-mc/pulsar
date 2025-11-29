package dev.zenfyr.pulsar.itemgroup.impl;

import dev.zenfyr.pulsar.itemgroup.ItemGroupAnimaton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.CreativeModeTab;

@Environment(EnvType.CLIENT)
public interface CreativeModeTabExtensions {

  default CreativeModeTab pulsar$setIconAnimation(ItemGroupAnimaton animation) {
    throw new IllegalStateException("Interface not implemented");
  }

  default ItemGroupAnimaton pulsar$getIconAnimation() {
    throw new IllegalStateException("Interface not implemented");
  }
}
