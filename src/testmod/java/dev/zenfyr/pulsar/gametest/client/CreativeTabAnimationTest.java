package dev.zenfyr.pulsar.gametest.client;

import com.mojang.math.Axis;
import dev.zenfyr.pulsar.api.creativetab.CreativeModeTabAnimaton;
import dev.zenfyr.pulsar.gametest.common.CreativeTabBuilderTest;
import dev.zenfyr.pulsar.gametest.util.AutoTest;
import dev.zenfyr.pulsar.gametest.util.client.ClientTestContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.itemgroup.CreativeGuiExtensions;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.Items;

@Environment(EnvType.CLIENT)
public class CreativeTabAnimationTest implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    var stack = Items.SPRUCE_SIGN.getDefaultInstance();
    CreativeModeTabAnimaton.setIconAnimation(
        CreativeTabBuilderTest.tab, (group, context, itemX, itemY, selected, isTopRow) -> {
          context.pose().pushPose();
          context.pose().translate(itemX, itemY, 100);
          context.pose().mulPose(Axis.ZN.rotationDegrees(Util.getMillis() * 0.05f));
          context.renderItem(stack, -8, -8);
          context.pose().popPose();
        });
  }

  @AutoTest
  void testCreativeModeTabAnimation(ClientTestContext context) {
    context.setScreen(minecraft -> new CreativeModeInventoryScreen(
        minecraft.player, minecraft.level.enabledFeatures(), false));
    context.waitForLevelTicks(10);
    context.executeForScreen(CreativeModeInventoryScreen.class, (client, screen) -> {
      ((CreativeGuiExtensions) screen).fabric_nextPage();
      return null;
    });
    context.takeScreenshot("creative-tab-animation");
    context.closeScreen();
  }
}
