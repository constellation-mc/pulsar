package dev.zenfyr.pulsar.api.client.fakelevel;

import com.google.common.base.Suppliers;
import dev.zenfyr.pulsar.api.client.events.AfterFirstReload;
import dev.zenfyr.pulsar.impl.client.fakelevel.FakeLevelImpl;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.ApiStatus;
import sun.misc.Unsafe;

/**
 * A fake {@link ClientLevel}, mainly to be used for rendering in GUIs.
 * Using this class is really not recommended, as it often breaks.
 */
@ApiStatus.Experimental
@UtilityClass
public class FakeLevel {

  private static final ThreadLocal<Boolean> LOADING = ThreadLocal.withInitial(() -> false);

  public static final Supplier<ClientLevel> INSTANCE = Suppliers.memoize(() -> {
    try {
      LOADING.set(true);

      var field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      Unsafe unsafe = (Unsafe) field.get(null);

      var level = (FakeLevelImpl) unsafe.allocateInstance(FakeLevelImpl.class);
      level.initHook();
      return level;

    } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
      throw new RuntimeException("Failed to init FakeLevelImpl!", e);
    } finally {
      LOADING.remove();
    }
  });

  /**
   * Returns if the current loading world is the fake {@link ClientLevel}.
   * @return if the current loading world is the fake {@link ClientLevel}.
   */
  public static boolean isLoading() {
    return LOADING.get();
  }

  public static boolean isFake(ClientLevel level) {
    return level instanceof FakeLevelImpl;
  }

  @ApiStatus.Internal
  public static void init() {
    AfterFirstReload.EVENT.listen(INSTANCE::get);
  }
}
