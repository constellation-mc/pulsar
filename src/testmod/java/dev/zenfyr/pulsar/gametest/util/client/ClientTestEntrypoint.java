package dev.zenfyr.pulsar.gametest.util.client;

public interface ClientTestEntrypoint {
  default void onClientTest(ClientTestContext context) {
    context.runAllForEntrypoint(this);
  }
}
