package dev.zenfyr.pulsar.api.event;

import dev.zenfyr.pulsar.impl.event.BusImpl;
import java.util.function.Function;

/**
 * Basic event bus.
 * @param <T> The event type.
 */
public interface Bus<T> {

  /**
   * Creates a simple array backed event bus.
   * @param type The event type.
   * @param factory A function to construct the {@link #invoker()}.
   * @return An array backed event bus.
   * @param <T> The event type.
   */
  static <T> Bus<T> create(Class<T> type, Function<T[], T> factory) {
    return new BusImpl<>(type, factory);
  }

  void listen(T listener);

  T invoker();
}
