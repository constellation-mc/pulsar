package dev.zenfyr.pulsar.test.server;

import com.mojang.logging.LogUtils;
import dev.zenfyr.pulsar.test.util.Utils;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class PulsarTestmodServer implements DedicatedServerModInitializer {

  private static final Logger log = LogUtils.getLogger();

  @Override
  public void onInitializeServer() {
    if (!Utils.ENABLED) return;

    ServerLifecycleEvents.SERVER_STARTING.register(server -> {
      var thread = new Thread(() -> {
        try {
          log.info("Started server test.");
          ServerTestContext context = new ServerTestContext(server);
          context.waitForOverworldTicks(200);

          FabricLoader.getInstance()
              .invokeEntrypoints(
                  "handy:server_test", ServerTestEntrypoint.class, e -> e.onServerTest(context));
          MixinEnvironment.getCurrentEnvironment().audit();

          Utils.runChecks(log);
          server.halt(false);
        } catch (Throwable t) {
          log.error("Failed server test!", t);
          System.exit(1);
        }
      });
      thread.start();
    });
  }
}
