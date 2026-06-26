package dev.zenfyr.pulsar.api.client.creativetab;

import dev.zenfyr.pulsar.impl.client.creativetab.CreativeModeTabExtensions;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.CreativeModeTab;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface CreativeModeTabAnimation {

  static CreativeModeTab setIconAnimation(
      CreativeModeTab group, CreativeModeTabAnimation animation) {
    ((CreativeModeTabExtensions) group).pulsar$setIconAnimation(animation);
    return group;
  }

  static Optional<CreativeModeTabAnimation> getIconAnimation(CreativeModeTab group) {
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
