package dev.zenfyr.pulsar.gametest.common;

import dev.zenfyr.pulsar.api.resources.ReloadListenerType;
import dev.zenfyr.pulsar.api.resources.ServerReloadListenersEvent;
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

  public static final ReloadListenerType<TestReloader> TYPE =
      ReloadListenerType.create(Identifier.fromNamespaceAndPath("pulsar", "test-reloader"));
  private static String trigger = null;

  @Override
  public void onInitialize() {
    ServerReloadListenersEvent.EVENT.listen(
        context -> context.register(TYPE.identifier(), new TestReloader(context)));

    // make sure that the reloader triggered
    Utils.addLateCheck("reload listener trigger set", () -> Objects.requireNonNull(trigger));
  }

  public static class TestReloader implements ResourceManagerReloadListener {

    private final ServerReloadListenersEvent.Context context;

    public TestReloader(ServerReloadListenersEvent.Context context) {
      this.context = context;
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
      trigger = "set";
      PulsarLog.logger()
          .info(
              "reload {}: {}",
              context.getListener(TYPE),
              context
                  .registryAccess()
                  .lookupOrThrow(Registries.DIMENSION_TYPE)
                  .get(ResourceKey.create(
                      Registries.DIMENSION_TYPE, Identifier.withDefaultNamespace("overworld"))));
    }

    @Override
    public String toString() {
      return TYPE.identifier().toString();
    }
  }
}
