package dev.zenfyr.pulsar.impl.mixin.itemgroup;

import dev.zenfyr.pulsar.itemgroup.ItemGroupAnimaton;
import dev.zenfyr.pulsar.itemgroup.impl.ItemGroupExtensions;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CreativeModeTab.class)
public class ItemGroupMixin implements ItemGroupExtensions {

  @Unique private ItemGroupAnimaton dark_matter$animation;

  @Override
  public CreativeModeTab dm$setIconAnimation(ItemGroupAnimaton animation) {
    this.dark_matter$animation = animation;
    return (CreativeModeTab) (Object) this;
  }

  @Override
  public ItemGroupAnimaton dm$getIconAnimation() {
    return dark_matter$animation;
  }
}
