package dev.zenfyr.pulsar.api.platform;

import dev.zenfyr.pulsar.impl.platform.PlatformImpl;
import java.nio.file.Path;

public interface Platform {

  static Platform getPlatform() {
    return PlatformImpl.INSTANCE;
  }

  CEnvType getEnvironment();

  boolean isDev();

  boolean isModLoaded(String mod);

  Path getConfigDir();

  Path getGameDir();

  String getName();
}
