package dev.zenfyr.pulsar.test.server;

public interface ServerTestEntrypoint {
  default void onServerTest(ServerTestContext context) {
    context.runAllForEntrypoint(this);
  }
}
