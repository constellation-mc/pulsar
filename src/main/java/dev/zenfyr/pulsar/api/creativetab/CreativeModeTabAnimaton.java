package dev.zenfyr.pulsar.api.creativetab;

import dev.zenfyr.pulsar.impl.creativetab.CreativeModeTabExtensions;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.CreativeModeTab;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface CreativeModeTabAnimaton {

  static CreativeModeTab setIconAnimation(
      CreativeModeTab group, CreativeModeTabAnimaton animation) {
    ((CreativeModeTabExtensions) group).pulsar$setIconAnimation(animation);
    return group;
  }

  static Optional<CreativeModeTabAnimaton> getIconAnimation(CreativeModeTab group) {
    return Optional.ofNullable(((CreativeModeTabExtensions) group).pulsar$getIconAnimation());
  }

  /**
   * Animates the icon for your creative tab.
   *
   * <p>This can draw anything you want</p>
   *
   * @param graphics the graphics used to render the screen
   * @param itemX the x-coordinate of the icon
   * @param itemY the y-coordinate of the icon
   */
  void animateIcon(
      CreativeModeTab tab,
      GuiGraphics graphics,
      int itemX,
      int itemY,
      boolean selected,
      boolean isTopRow);
}
