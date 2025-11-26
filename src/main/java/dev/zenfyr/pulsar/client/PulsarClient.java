package dev.zenfyr.pulsar.client;

import dev.zenfyr.pulsar.client.fakeworld.FakeWorld;
import net.fabricmc.api.ClientModInitializer;

public class PulsarClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    FakeWorld.init();
  }
}
