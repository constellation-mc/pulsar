package dev.zenfyr.pulsar.impl.mixin.client.itemgroup;

import dev.zenfyr.pulsar.itemgroup.ItemGroupAnimaton;
import dev.zenfyr.pulsar.itemgroup.impl.ItemGroupExtensions;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CreativeModeTab.class)
public class ItemGroupMixin implements ItemGroupExtensions {

  @Unique private ItemGroupAnimaton pulsar$animation;

  @Override
  public CreativeModeTab pulsar$setIconAnimation(ItemGroupAnimaton animation) {
    this.pulsar$animation = animation;
    return (CreativeModeTab) (Object) this;
  }

  @Override
  public ItemGroupAnimaton pulsar$getIconAnimation() {
    return pulsar$animation;
  }
}
