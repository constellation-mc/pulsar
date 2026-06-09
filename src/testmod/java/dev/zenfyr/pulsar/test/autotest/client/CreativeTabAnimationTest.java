package dev.zenfyr.pulsar.test.autotest.client;

import dev.zenfyr.pulsar.creativetab.CreativeModeTabAnimaton;
import dev.zenfyr.pulsar.test.autotest.CreativeTabBuilderTest;
import dev.zenfyr.pulsar.test.client.ClientTestContext;
import dev.zenfyr.pulsar.test.client.ClientTestEntrypoint;
import dev.zenfyr.pulsar.test.util.AutoTest;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.Items;

@Environment(EnvType.CLIENT)
public class CreativeTabAnimationTest implements ClientModInitializer, ClientTestEntrypoint {

  @Override
  public void onInitializeClient() {
    var stack = Items.SPRUCE_SIGN.getDefaultInstance();
    CreativeModeTabAnimaton.setIconAnimation(
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
