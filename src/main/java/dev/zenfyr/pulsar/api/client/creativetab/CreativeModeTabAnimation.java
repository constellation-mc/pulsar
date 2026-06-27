package dev.zenfyr.pulsar.api.client.creativetab;

import dev.zenfyr.pulsar.impl.client.creativetab.CreativeModeTabExtensions;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.CreativeModeTab;

/**
 * Utils to set a custom icon for creative mod tabs.
 */
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
   * Extracts the icon animation state for rendering.
   *
   * @param graphics the graphics extractor
   * @param itemX the x-coordinate of the icon
   * @param itemY the y-coordinate of the icon
   */
  void extractAnimation(
      CreativeModeTab tab,
      GuiGraphicsExtractor graphics,
      int itemX,
      int itemY,
      boolean selected,
      boolean isTopRow);
}
