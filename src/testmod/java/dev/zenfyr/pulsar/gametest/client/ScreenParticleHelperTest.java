package dev.zenfyr.pulsar.gametest.client;

import dev.zenfyr.pulsar.api.client.particles.ItemStackParticle;
import dev.zenfyr.pulsar.api.client.particles.ScreenParticles;
import dev.zenfyr.pulsar.api.client.particles.VanillaParticles;
import dev.zenfyr.pulsar.api.util.MathUtil;
import dev.zenfyr.pulsar.gametest.util.AutoTest;
import dev.zenfyr.pulsar.gametest.util.client.ClientTestContext;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.Items;

public class ScreenParticleHelperTest {

  @AutoTest
  void testCustomScreenParticles(ClientTestContext context) {
    context.waitForLevelTicks(10);
    context.submitAndWait(client -> {
      var particles = ScreenParticles.get(client);
      for (int i = 0; i < 10; i++) {
        particles.addParticle(new ItemStackParticle(
            60,
            40,
            MathUtil.nextDouble(-0.2, 0.8),
            MathUtil.nextDouble(-0.2, 0.8),
            Items.AMETHYST_SHARD.getDefaultInstance()));
      }
      return null;
    });
    context.takeScreenshot("screen-particles-custom");
  }

  @AutoTest
  void testVanillaScreenParticles(ClientTestContext context) {
    context.waitForLevelTicks(10);
    context.submitAndWait(client -> {
      var particles = ScreenParticles.get(client);
      particles.addParticles(
          VanillaParticles.create(ParticleTypes.END_ROD, 40, 40, 0.7, 0.7, 0.07, 10));
      particles.addParticles(
          VanillaParticles.create(ParticleTypes.ANGRY_VILLAGER, 60, 40, 0.7, 0.7, 0.07, 10));
      return null;
    });
    context.takeScreenshot("screen-particles-vanilla");
  }
}
