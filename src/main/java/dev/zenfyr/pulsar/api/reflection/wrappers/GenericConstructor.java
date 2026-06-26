package dev.zenfyr.pulsar.api.reflection.wrappers;

import dev.zenfyr.pulsar.api.reflection.Reflect;
import dev.zenfyr.pulsar.api.util.ExceptionUtil;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public interface GenericConstructor<O> {
  O construct(Object... args);

  GenericConstructor<O> accessible(boolean accessible);

  static <O> GenericConstructor<O> of(Class<O> cls, Class<?>... args) {
    return Reflect.findConstructor(cls, args)
        .map(GenericConstructor::of)
        .orElseThrow(() -> new IllegalStateException(
            "No such constructor %s(%s)!".formatted(cls, Arrays.toString(args))));
  }

  static <O> GenericConstructor<O> of(Constructor<O> ctx) {
    return new GenericConstructor<>() {
      @Override
      public O construct(Object... args) {
        return ExceptionUtil.supply(() -> ctx.newInstance(args));
      }

      @Override
      public GenericConstructor<O> accessible(boolean accessible) {
        ctx.setAccessible(accessible);
        return this;
      }
    };
  }
}
