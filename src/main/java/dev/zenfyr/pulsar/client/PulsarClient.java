package dev.zenfyr.pulsar.client;

import dev.zenfyr.pulsar.client.fakeworld.FakeLevel;
import net.fabricmc.api.ClientModInitializer;

public class PulsarClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    FakeLevel.init();
  }
}
