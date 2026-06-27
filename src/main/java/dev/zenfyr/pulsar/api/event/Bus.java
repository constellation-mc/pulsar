package dev.zenfyr.pulsar.api.event;

import dev.zenfyr.pulsar.impl.event.BusImpl;
import java.util.function.Function;

public interface Bus<T> {

  static <T> Bus<T> create(Class<T> type, Function<T[], T> factory) {
    return new BusImpl<>(type, factory);
  }

  void listen(T listener);

  T invoker();
}
