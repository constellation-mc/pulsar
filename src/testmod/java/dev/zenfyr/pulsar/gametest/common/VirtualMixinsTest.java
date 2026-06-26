package dev.zenfyr.pulsar.gametest.common;

import dev.zenfyr.pulsar.mixin.VirtualMixins;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.assertj.core.api.Assertions;
import org.spongepowered.asm.mixin.Mixins;

public class VirtualMixinsTest implements PreLaunchEntrypoint, ModInitializer {

  private static final String CONFIG = "pulsar-test-config";
  private static final String CONTENTS = """
                  {
                    "required": false,
                    "minVersion": "0.8",
                    "package": "dev.zenfyr.pulsar.gametest.common.virtualmixins",
                    "compatibilityLevel": "JAVA_17",
                    "mixins": [
                        "VirtualMixinsDummyMixin"
                    ],
                    "client": [
                    ],
                    "injectors": {
                      "defaultRequire": 0
                    }
                  }""";

  @Override
  public void onInitialize() {
    // test if the config was consumed
    Assertions.assertThat(Mixins.getConfigs().stream())
        .noneMatch(config -> CONFIG.equals(config.getName()));

    // test if the mixin applied
    Assertions.assertThat(VirtualMixinsDummyTarget.test()).isEqualTo("mod");
  }

  @Override
  public void onPreLaunch() {
    VirtualMixins.addMixins(acceptor ->
        acceptor.add(CONFIG, new ByteArrayInputStream(CONTENTS.getBytes(StandardCharsets.UTF_8))));
  }
}
