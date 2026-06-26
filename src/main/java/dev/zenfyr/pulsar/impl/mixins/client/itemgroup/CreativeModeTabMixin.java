package dev.zenfyr.pulsar.impl.mixins.client.itemgroup;

import dev.zenfyr.pulsar.api.creativetab.CreativeModeTabAnimaton;
import dev.zenfyr.pulsar.impl.creativetab.CreativeModeTabExtensions;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabMixin implements CreativeModeTabExtensions {

  @Unique private CreativeModeTabAnimaton pulsar$animation;

  @Override
  public CreativeModeTab pulsar$setIconAnimation(CreativeModeTabAnimaton animation) {
    this.pulsar$animation = animation;
    return (CreativeModeTab) (Object) this;
  }

  @Override
  public CreativeModeTabAnimaton pulsar$getIconAnimation() {
    return pulsar$animation;
  }
}
