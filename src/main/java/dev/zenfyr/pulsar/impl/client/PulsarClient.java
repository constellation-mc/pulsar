package dev.zenfyr.pulsar.impl.client;

import dev.zenfyr.pulsar.api.client.events.AfterFirstReload;
import dev.zenfyr.pulsar.api.client.fakelevel.BrightLightTexture;
import dev.zenfyr.pulsar.api.client.fakelevel.FakeLevel;
import net.fabricmc.api.ClientModInitializer;

public class PulsarClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    FakeLevel.init();
    AfterFirstReload.EVENT.listen(BrightLightTexture::init);
  }
}
