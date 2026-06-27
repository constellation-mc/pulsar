package dev.zenfyr.pulsar.impl.platform;

import dev.zenfyr.pulsar.api.platform.CEnvType;
import dev.zenfyr.pulsar.api.platform.Platform;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public class PlatformImpl implements Platform {

  public static Platform INSTANCE = new PlatformImpl();

  @Override
  public CEnvType getEnvironment() {
    return switch (FabricLoader.getInstance().getEnvironmentType()) {
      case CLIENT -> CEnvType.CLIENT;
      case SERVER -> CEnvType.SERVER;
    };
  }

  @Override
  public boolean isDev() {
    return FabricLoader.getInstance().isDevelopmentEnvironment();
  }

  @Override
  public boolean isModLoaded(String mod) {
    return FabricLoader.getInstance().isModLoaded(mod);
  }

  @Override
  public Path getConfigDir() {
    return FabricLoader.getInstance().getConfigDir();
  }

  @Override
  public Path getGameDir() {
    return FabricLoader.getInstance().getGameDir();
  }

  @Override
  public String getName() {
    return "Fabric";
  }

  @Override
  public String toString() {
    return "FabricPlatform";
  }
}
