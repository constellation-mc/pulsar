package dev.zenfyr.pulsar.impl.mixins.creativetab;

import dev.zenfyr.pulsar.impl.creativetab.CreativeModeTabDuck;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabMixin implements CreativeModeTabDuck {

  @Unique private boolean pulsar$Tab = false;

  @Override
  public boolean pulsar$isPulsarTab() {
    return this.pulsar$Tab;
  }

  @Override
  public void pulsar$isPulsarTab(boolean is) {
    this.pulsar$Tab = is;
  }
}
