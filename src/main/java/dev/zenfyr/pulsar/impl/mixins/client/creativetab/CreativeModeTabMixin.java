package dev.zenfyr.pulsar.impl.mixins.client.creativetab;

import dev.zenfyr.pulsar.api.client.creativetab.CreativeModeTabAnimation;
import dev.zenfyr.pulsar.impl.client.creativetab.CreativeModeTabExtensions;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabMixin implements CreativeModeTabExtensions {

  @Unique private CreativeModeTabAnimation pulsar$animation;

  @Override
  public CreativeModeTab pulsar$setIconAnimation(CreativeModeTabAnimation animation) {
    this.pulsar$animation = animation;
    return (CreativeModeTab) (Object) this;
  }

  @Override
  public CreativeModeTabAnimation pulsar$getIconAnimation() {
    return pulsar$animation;
  }
}
