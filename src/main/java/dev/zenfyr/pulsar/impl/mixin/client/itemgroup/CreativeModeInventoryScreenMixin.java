package dev.zenfyr.pulsar.impl.mixin.client.itemgroup;

import com.llamalad7.mixinextras.sugar.Local;
import dev.zenfyr.pulsar.creativetab.impl.CreativeModeTabExtensions;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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
      method = "extractTabButton",
      cancellable = true)
  private void pulsar$drawGroupIcon(
      GuiGraphicsExtractor graphics,
      int mouseX,
      int mouseY,
      CreativeModeTab tab,
      CallbackInfo ci,
      @Local(index = 11) int iconX,
      @Local(index = 12) int iconY,
      @Local(index = 5) boolean selected,
      @Local(index = 6) boolean isTop) {
    if (((CreativeModeTabExtensions) tab).pulsar$getIconAnimation() != null) {
      ((CreativeModeTabExtensions) tab)
          .pulsar$getIconAnimation()
          .extractAnimation(tab, graphics, iconX, iconY, selected, isTop);
      ci.cancel();
    }
  }
}
