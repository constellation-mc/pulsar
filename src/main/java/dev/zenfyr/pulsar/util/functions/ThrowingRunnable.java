package dev.zenfyr.pulsar.util.functions;

@FunctionalInterface
public interface ThrowingRunnable<E extends Throwable> {
  void run() throws E;
}
