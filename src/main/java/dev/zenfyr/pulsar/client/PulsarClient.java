package dev.zenfyr.pulsar.client;

import dev.zenfyr.pulsar.client.events.AfterFirstReload;
import dev.zenfyr.pulsar.client.fakelevel.FakeLevel;
import dev.zenfyr.pulsar.client.particles.ScreenParticleHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.core.particles.ParticleTypes;

public class PulsarClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    FakeLevel.init();

    AfterFirstReload.EVENT.register(() -> {
      ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
        if (true) {
          ScreenParticleHelper.addParticles(ParticleTypes.END_ROD, 40, 40, 1.25, 1.25, 1, 10);
        }
      });
    });
  }
}
