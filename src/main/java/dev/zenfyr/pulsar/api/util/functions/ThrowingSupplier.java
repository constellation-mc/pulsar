package dev.zenfyr.pulsar.api.util.functions;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Throwable> {
  T get() throws E;
}
