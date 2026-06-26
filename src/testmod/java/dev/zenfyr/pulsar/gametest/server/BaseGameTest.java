package dev.zenfyr.pulsar.gametest.server;

import com.mojang.logging.LogUtils;
import dev.zenfyr.pulsar.gametest.util.Utils;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class BaseGameTest {

  @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
  public void mixinAudit(GameTestHelper helper) {
    MixinEnvironment.getCurrentEnvironment().audit();
    helper.succeed();
  }

  @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
  public void applyLateChecks(GameTestHelper helper) {
    Utils.runChecks(LogUtils.getLogger());
    helper.succeed();
  }
}
