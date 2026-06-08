package dev.zenfyr.pulsar.test.client;

public interface ClientTestEntrypoint {
  default void onClientTest(ClientTestContext context) {
    context.runAllForEntrypoint(this);
  }
}
