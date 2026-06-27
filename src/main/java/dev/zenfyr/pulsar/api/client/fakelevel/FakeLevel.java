package dev.zenfyr.pulsar.api.client.fakelevel;

import dev.zenfyr.pulsar.impl.client.fakelevel.FakeLevelImpl;
import lombok.experimental.UtilityClass;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.ApiStatus;

/**
 * A fake {@link ClientLevel}, mainly to be used for rendering in GUIs.
 * Using this class is really not recommended, as it often breaks.
 */
@ApiStatus.Experimental
@UtilityClass
public class FakeLevel {

  public static ClientLevel getInstance() {
    return FakeLevelImpl.INSTANCE.get();
  }

  public static boolean isFake(ClientLevel level) {
    return level instanceof FakeLevelImpl;
  }
}
