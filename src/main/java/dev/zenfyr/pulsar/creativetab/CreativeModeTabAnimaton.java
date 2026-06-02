package dev.zenfyr.pulsar.creativetab;

import dev.zenfyr.pulsar.creativetab.impl.CreativeModeTabExtensions;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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
