package dev.zenfyr.pulsar.api.util.functions;

@FunctionalInterface
public interface ThrowingRunnable<E extends Throwable> {
  void run() throws E;
}
