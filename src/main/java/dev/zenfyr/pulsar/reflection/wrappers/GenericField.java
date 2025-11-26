package dev.zenfyr.pulsar.reflection.wrappers;

import dev.zenfyr.pulsar.reflection.Reflect;
import dev.zenfyr.pulsar.util.ExceptionUtil;
import java.lang.reflect.Field;

@SuppressWarnings("unchecked")
public interface GenericField<O, T> {
  T get(O obj);

  void set(O obj, T value);

  GenericField<O, T> accessible(boolean accessible);

  static <O, T> GenericField<O, T> of(Class<O> cls, String name) {
    return (GenericField<O, T>) Reflect.findField(cls, name)
        .map(GenericField::of)
        .orElseThrow(() -> new IllegalStateException("No such field %s.%s!".formatted(cls, name)));
  }

  static <O, T> GenericField<O, T> of(Field field) {
    return new GenericField<>() {
      @Override
      public T get(O obj) {
        return (T) ExceptionUtil.supply(() -> field.get(obj));
      }

      @Override
      public void set(O obj, T value) {
        ExceptionUtil.run(() -> field.set(obj, value));
      }

      @Override
      public GenericField<O, T> accessible(boolean accessible) {
        field.setAccessible(accessible);
        return this;
      }
    };
  }
}
