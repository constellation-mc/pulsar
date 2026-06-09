package dev.zenfyr.pulsar.test.autotest;

import dev.zenfyr.pulsar.resources.ReloaderType;
import dev.zenfyr.pulsar.resources.ServerReloadersEvent;
import dev.zenfyr.pulsar.test.util.Utils;
import dev.zenfyr.pulsar.util.PulsarLog;
import java.util.Objects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;

public class ServerReloadersEventTest implements ModInitializer {

  public static final ReloaderType<TestReloader> TYPE =
      ReloaderType.create(Identifier.fromNamespaceAndPath("pulsar", "test-reloader"));
  private static String trigger = null;

  @Override
  public void onInitialize() {
    ServerReloadersEvent.EVENT.register(context -> context.register(new TestReloader(context)));

    // make sure that the reloader triggered
    Utils.addLateCheck("reload listener trigger set", () -> Objects.requireNonNull(trigger));
  }

  public static class TestReloader implements SimpleSynchronousResourceReloadListener {

    private final ServerReloadersEvent.Context context;

    public TestReloader(ServerReloadersEvent.Context context) {
      this.context = context;
    }

    @Override
    public Identifier getFabricId() {
      return TYPE.identifier();
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
                  .lookupOrThrow(Registries.DIMENSION_TYPE)
                  .get(ResourceKey.create(
                      Registries.DIMENSION_TYPE, Identifier.withDefaultNamespace("overworld"))));
    }
  }
}
