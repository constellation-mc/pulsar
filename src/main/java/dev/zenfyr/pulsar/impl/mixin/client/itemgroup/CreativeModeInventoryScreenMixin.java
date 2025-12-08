package dev.zenfyr.pulsar.impl.mixin.client.itemgroup;

import com.llamalad7.mixinextras.sugar.Local;
import dev.zenfyr.pulsar.creativetab.impl.CreativeModeTabExtensions;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin
    extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

  public CreativeModeInventoryScreenMixin(
      CreativeModeInventoryScreen.ItemPickerMenu screenHandler,
      Inventory playerInventory,
      Component text) {
    super(screenHandler, playerInventory, text);
  }

  @Inject(
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/world/item/CreativeModeTab;getIconItem()Lnet/minecraft/world/item/ItemStack;",
              shift = At.Shift.BEFORE),
      method = "renderTabButton",
      cancellable = true)
  private void pulsar$drawGroupIcon(
      GuiGraphics context,
      CreativeModeTab group,
      CallbackInfo ci,
      @Local(index = 3) boolean bl,
      @Local(index = 4) boolean bl2,
      @Local(index = 9) int l,
      @Local(index = 10) int m) {
    if (((CreativeModeTabExtensions) group).pulsar$getIconAnimation() != null) {
      ((CreativeModeTabExtensions) group)
          .pulsar$getIconAnimation()
          .animateIcon(group, context, l, m, bl, bl2);
      ci.cancel();
    }
  }
}
