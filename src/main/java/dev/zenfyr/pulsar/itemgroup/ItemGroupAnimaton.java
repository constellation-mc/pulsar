package dev.zenfyr.pulsar.itemgroup;

import dev.zenfyr.pulsar.itemgroup.impl.ItemGroupExtensions;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.CreativeModeTab;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface ItemGroupAnimaton {

  static CreativeModeTab setIconAnimation(CreativeModeTab group, ItemGroupAnimaton animation) {
    ((ItemGroupExtensions) group).dm$setIconAnimation(animation);
    return group;
  }

  static Optional<ItemGroupAnimaton> getIconAnimation(CreativeModeTab group) {
    return Optional.ofNullable(((ItemGroupExtensions) group).dm$getIconAnimation());
  }

  /**
   * Animates the icon for your item group.
   *
   * <p>This can draw anything you want</p>
   *
   * @param context the matrix stack used to render the screen
   * @param itemX the x-coordinate of the icon
   * @param itemY the y-coordinate of the icon
   */
  void animateIcon(
      CreativeModeTab group,
      GuiGraphics context,
      int itemX,
      int itemY,
      boolean selected,
      boolean isTopRow);
}
