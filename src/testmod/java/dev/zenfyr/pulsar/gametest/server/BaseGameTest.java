package dev.zenfyr.pulsar.gametest.server;

import com.mojang.logging.LogUtils;
import dev.zenfyr.pulsar.gametest.util.Utils;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class BaseGameTest {

  @GameTest()
  public void mixinAudit(GameTestHelper helper) {
    MixinEnvironment.getCurrentEnvironment().audit();
    helper.succeed();
  }

  @GameTest()
  public void applyLateChecks(GameTestHelper helper) {
    Utils.runChecks(LogUtils.getLogger());
    helper.succeed();
  }
}
