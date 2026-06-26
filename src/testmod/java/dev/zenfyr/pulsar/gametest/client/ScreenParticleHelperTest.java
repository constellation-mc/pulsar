package dev.zenfyr.pulsar.gametest.client;

import dev.zenfyr.pulsar.api.client.particles.ItemStackParticle;
import dev.zenfyr.pulsar.api.client.particles.ScreenParticleHelper;
import dev.zenfyr.pulsar.api.util.MathUtil;
import dev.zenfyr.pulsar.gametest.util.AutoTest;
import dev.zenfyr.pulsar.gametest.util.client.ClientTestContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.Items;

@Environment(EnvType.CLIENT)
public class ScreenParticleHelperTest {

  @AutoTest
  void testCustomScreenParticles(ClientTestContext context) {
    context.waitForLevelTicks(10);
    context.submitAndWait(client -> {
      ScreenParticleHelper.addParticles(
          () -> new ItemStackParticle(
              60,
              40,
              MathUtil.nextDouble(-0.2, 0.8),
              MathUtil.nextDouble(-0.2, 0.8),
              Items.AMETHYST_SHARD.getDefaultInstance()),
          10);
      return null;
    });
    context.takeScreenshot("screen-particles-custom");
  }

  @AutoTest
  void testVanillaScreenParticles(ClientTestContext context) {
    context.waitForLevelTicks(10);
    context.submitAndWait(client -> {
      ScreenParticleHelper.addParticles(ParticleTypes.END_ROD, 40, 40, 0.7, 0.7, 0.07, 10);
      ScreenParticleHelper.addParticles(ParticleTypes.ANGRY_VILLAGER, 60, 40, 0.7, 0.7, 0.07, 10);
      return null;
    });
    context.takeScreenshot("screen-particles-vanilla");
  }
}
