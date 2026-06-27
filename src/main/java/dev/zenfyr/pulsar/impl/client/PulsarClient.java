package dev.zenfyr.pulsar.impl.client;

import dev.zenfyr.pulsar.api.client.events.AfterFirstReload;
import dev.zenfyr.pulsar.impl.client.fakelevel.BrightLightTextureImpl;
import dev.zenfyr.pulsar.impl.client.fakelevel.FakeLevelImpl;
import net.fabricmc.api.ClientModInitializer;

public class PulsarClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    FakeLevelImpl.init();
    AfterFirstReload.EVENT.listen(BrightLightTextureImpl::init);
  }
}
