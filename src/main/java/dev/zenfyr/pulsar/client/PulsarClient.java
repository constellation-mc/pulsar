package dev.zenfyr.pulsar.client;

import dev.zenfyr.pulsar.client.events.AfterFirstReload;
import dev.zenfyr.pulsar.client.fakelevel.BrightLightTexture;
import dev.zenfyr.pulsar.client.fakelevel.FakeLevel;
import net.fabricmc.api.ClientModInitializer;

public class PulsarClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    FakeLevel.init();
    AfterFirstReload.EVENT.register(BrightLightTexture::init);
  }
}
