package dev.zenfyr.pulsar.api.mixin;

import dev.zenfyr.pulsar.api.reflection.wrappers.GenericField;
import dev.zenfyr.pulsar.api.reflection.wrappers.GenericMethod;
import dev.zenfyr.pulsar.api.util.tuple.Tuple;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.util.function.Consumer;
import lombok.NonNull;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.FabricUtil;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.MixinService;

/**
 * Allows adding new "virtual" mixin configs. This temporarily replaces the mixin service and injects the configs using a ThreadLocal. This <b>MUST</b> be run at preLaunch, while no classes are transformed!
 * <p>
 * It's recommended to decorate the configs with {@code 'fabric-modId'} to help with debugging.
 * </p>
 * <p>
 * There's no reason to use this for regular configs, which are available on the classpath.
 * </p>
 * <p>
 * The Mixin framework does not seem to care about the "realness" of the configs, but some static analysis tools might run into issues. (e.g. Connector before beta 38)
 * </p>
 */
@ApiStatus.Experimental
public class VirtualMixins {

  private static final ThreadLocal<Tuple<String, InputStream>> CONFIG =
      ThreadLocal.withInitial(() -> null);

  private static final GenericMethod<?, MixinService> GET_INSTANCE =
      GenericMethod.of(MixinService.class, "getInstance");
  private static final GenericField<MixinService, IMixinService> SERVICE =
      GenericField.of(MixinService.class, "service");

  public static void addMixins(Consumer<Acceptor> consumer) {
    IMixinService service = MixinService.getService();
    injectService(service);
    consumer.accept(VirtualMixins::add);
    dejectService(service);
  }

  private static void add(@NonNull String configName, @NonNull InputStream stream) {
    Mixins.getConfigs().stream()
        .filter(config -> config.getName().equals(configName))
        .findFirst()
        .ifPresent(config -> {
          throw new IllegalStateException("Config name %s is already in use by %s!"
              .formatted(config.getName(), FabricUtil.getModId(config.getConfig())));
        });

    try {
      CONFIG.set(Tuple.of(configName, stream));
      Mixins.addConfiguration(configName);
    } finally {
      CONFIG.remove();
    }
  }

  private static void injectService(IMixinService currentService) {
    IMixinService service = (IMixinService) Proxy.newProxyInstance(
        VirtualMixins.class.getClassLoader(),
        new Class[] {IMixinService.class},
        (proxy, method, args) -> {
          if (method.getName().equals("getResourceAsStream")) {
            if (args[0] instanceof String s) {
              var tuple = CONFIG.get();
              if (tuple != null && tuple.left().equals(s)) {
                return tuple.right();
              }
            }
          }

          return method.invoke(currentService, args);
        });
    MixinService serviceProxy = GET_INSTANCE.accessible(true).invoke(null);
    SERVICE.accessible(true).set(serviceProxy, service);
  }

  private static void dejectService(IMixinService realService) {
    MixinService serviceProxy = GET_INSTANCE.accessible(true).invoke(null);
    SERVICE.accessible(true).set(serviceProxy, realService);
  }

  public interface Acceptor {
    void add(String configName, InputStream stream);
  }
}
