package dev.zenfyr.pulsar.gametest.common;

import dev.zenfyr.pulsar.api.resources.ReloaderType;
import dev.zenfyr.pulsar.api.resources.ServerReloadersEvent;
import dev.zenfyr.pulsar.gametest.util.Utils;
import dev.zenfyr.pulsar.impl.PulsarLog;
import java.util.Objects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class ServerReloadersEventTest implements ModInitializer {

  public static final ReloaderType<TestReloader> TYPE =
      ReloaderType.create(new ResourceLocation("pulsar", "test-reloader"));
  private static String trigger = null;

  @Override
  public void onInitialize() {
    ServerReloadersEvent.EVENT.listen(context -> context.register(new TestReloader(context)));

    // make sure that the reloader triggered
    Utils.addLateCheck("reload listener trigger set", () -> Objects.requireNonNull(trigger));
  }

  public static class TestReloader implements SimpleSynchronousResourceReloadListener {

    private final ServerReloadersEvent.Context context;

    public TestReloader(ServerReloadersEvent.Context context) {
      this.context = context;
    }

    @Override
    public ResourceLocation getFabricId() {
      return TYPE.location();
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
      trigger = "set";
      PulsarLog.logger()
          .info(
              "reload {}: {}",
              context.reloader(TYPE).getFabricId(),
              context
                  .registryAccess()
                  .registryOrThrow(Registries.DIMENSION_TYPE)
                  .get(new ResourceLocation("overworld")));
    }
  }
}
