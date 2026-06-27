package dev.zenfyr.pulsar.api.platform;

import dev.zenfyr.pulsar.api.util.functions.ThrowingSupplier;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SupportUtil {

  public <T, F extends T, S extends T> T support(
      String mod, Supplier<F> expected, Supplier<S> fallback) {
    return support(Platform.getPlatform().isModLoaded(mod), expected, fallback);
  }

  public <T, F extends T, S extends T> T fallback(
      String mod, ThrowingSupplier<F, Throwable> expected, Supplier<S> fallback) {
    return fallback(Platform.getPlatform().isModLoaded(mod), expected, fallback);
  }

  public <T, F extends T, S extends T> T support(
      CEnvType cond, Supplier<F> expected, Supplier<S> fallback) {
    return support(cond == Platform.getPlatform().getEnvironment(), expected, fallback);
  }

  public <T, F extends T, S extends T> T fallback(
      CEnvType cond, ThrowingSupplier<F, Throwable> expected, Supplier<S> fallback) {
    return fallback(cond == Platform.getPlatform().getEnvironment(), expected, fallback);
  }

  public <T, F extends T, S extends T> T support(
      boolean cond, Supplier<F> expected, Supplier<S> fallback) {
    return cond ? expected.get() : fallback.get();
  }

  public <T, F extends T, S extends T> T fallback(
      boolean cond, ThrowingSupplier<F, Throwable> expected, Supplier<S> fallback) {
    try {
      return cond ? expected.get() : fallback.get();
    } catch (Throwable e) {
      return fallback.get();
    }
  }
}
