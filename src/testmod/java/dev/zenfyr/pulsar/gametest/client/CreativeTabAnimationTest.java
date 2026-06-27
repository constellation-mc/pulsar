package dev.zenfyr.pulsar.gametest.client;

import dev.zenfyr.pulsar.api.client.creativetab.CreativeModeTabAnimation;
import dev.zenfyr.pulsar.gametest.common.CreativeTabBuilderTest;
import dev.zenfyr.pulsar.gametest.util.AutoTest;
import dev.zenfyr.pulsar.gametest.util.client.ClientTestContext;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.Items;

public class CreativeTabAnimationTest implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    var stack = Items.SPRUCE_SIGN.getDefaultInstance();
    CreativeModeTabAnimation.setIconAnimation(
        CreativeTabBuilderTest.tab, (group, context, itemX, itemY, selected, isTopRow) -> {
          context.pose().pushMatrix();
          context.pose().translate(itemX, itemY);
          context.pose().rotate(Util.getMillis() * 0.05f);
          context.renderItem(stack, -8, -8);
          context.pose().popMatrix();
        });
  }

  @AutoTest
  void testCreativeModeTabAnimation(ClientTestContext context) {
    context.setScreen(minecraft -> new CreativeModeInventoryScreen(
        minecraft.player, minecraft.level.enabledFeatures(), false));
    context.waitForLevelTicks(10);
    context.executeForScreen(CreativeModeInventoryScreen.class, (client, screen) -> {
      screen.switchToNextPage();
      return null;
    });
    context.takeScreenshot("creative-tab-animation");
    context.closeScreen();
  }
}
