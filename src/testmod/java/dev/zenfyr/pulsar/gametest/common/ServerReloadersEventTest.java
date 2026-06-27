package dev.zenfyr.pulsar.gametest.common;

import dev.zenfyr.pulsar.api.resources.ReloaderType;
import dev.zenfyr.pulsar.api.resources.ServerReloadersEvent;
import dev.zenfyr.pulsar.gametest.util.Utils;
import dev.zenfyr.pulsar.impl.PulsarLog;
import java.util.Objects;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class ServerReloadersEventTest implements ModInitializer {

  public static final ReloaderType<TestReloader> TYPE =
      ReloaderType.create(Identifier.fromNamespaceAndPath("pulsar", "test-reloader"));
  private static String trigger = null;

  @Override
  public void onInitialize() {
    ServerReloadersEvent.EVENT.listen(
        context -> context.register(TYPE.identifier(), new TestReloader(context)));

    // make sure that the reloader triggered
    Utils.addLateCheck("reload listener trigger set", () -> Objects.requireNonNull(trigger));
  }

  public static class TestReloader implements ResourceManagerReloadListener {

    private final ServerReloadersEvent.Context context;

    public TestReloader(ServerReloadersEvent.Context context) {
      this.context = context;
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
      trigger = "set";
      PulsarLog.logger()
          .info(
              "reload {}: {}",
              TYPE.identifier(),
              context
                  .registryAccess()
                  .lookupOrThrow(Registries.DIMENSION_TYPE)
                  .get(ResourceKey.create(
                      Registries.DIMENSION_TYPE, Identifier.withDefaultNamespace("overworld"))));
    }
  }
}
