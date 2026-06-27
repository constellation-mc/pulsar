package dev.zenfyr.pulsar.gametest.util.client;

import com.mojang.logging.LogUtils;
import dev.zenfyr.pulsar.api.client.events.AfterFirstReload;
import dev.zenfyr.pulsar.gametest.util.TestRunner;
import dev.zenfyr.pulsar.gametest.util.Utils;
import java.util.regex.Pattern;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.CrashReport;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class PulsarTestmodClient implements ClientModInitializer {

  private static final Logger log = LogUtils.getLogger();

  private static final Pattern RESERVED_WINDOWS_NAMES = Pattern.compile(
      ".*\\.|(?:COM|CLOCK\\$|CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\..*)?",
      Pattern.CASE_INSENSITIVE);

  @Override
  public void onInitializeClient() {
    if (!Utils.ENABLED) return;
    Minecraft client = Minecraft.getInstance();

    AfterFirstReload.EVENT.listen(() -> client.execute(() -> {
      try {
        String levelName = "pulsar_test_"
            + FabricLoader.getInstance()
                .getModContainer("minecraft")
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();

        for (char c : SharedConstants.ILLEGAL_FILE_CHARACTERS)
          levelName = levelName.replace(c, '_');
        levelName = levelName.replaceAll("[./\"]", "_");
        if (RESERVED_WINDOWS_NAMES.matcher(levelName).matches()) levelName = "_" + levelName + "_";

        if (!client.getLevelSource().levelExists(levelName)) {
          client
              .createWorldOpenFlows()
              .createFreshLevel(
                  levelName,
                  new LevelSettings(
                      levelName,
                      GameType.CREATIVE,
                      new LevelSettings.DifficultySettings(Difficulty.PEACEFUL, false, false),
                      true,
                      WorldDataConfiguration.DEFAULT),
                  new WorldOptions(0, true, false),
                  registryManager -> registryManager
                      .lookupOrThrow(Registries.WORLD_PRESET)
                      .getOrThrow(WorldPresets.FLAT)
                      .value()
                      .createWorldDimensions(),
                  new TitleScreen());
        } else {
          client
              .createWorldOpenFlows()
              .openWorld(levelName, () -> Minecraft.getInstance().gui.setScreen(new TitleScreen()));
        }
      } catch (Throwable t) {
        CrashReport report = CrashReport.forThrowable(t, "Setting tests world");
        Minecraft.crash(Minecraft.getInstance(), Minecraft.getInstance().gameDirectory, report, -1);
      }
    }));

    var thread = new Thread(() -> {
      try {
        log.info("Started client test.");
        ClientTestContext context = new ClientTestContext(client);
        context.waitForLevelTicks(200);
        GLFW.glfwShowWindow(client.getWindow().handle());

        FabricLoader.getInstance()
            .getEntrypoints("pulsar:client_test", Object.class)
            .forEach(entrypoint -> TestRunner.runTests(entrypoint, context));
        MixinEnvironment.getCurrentEnvironment().audit();

        Utils.runChecks(log);
        client.stop();
      } catch (Throwable t) {
        log.error("Failed client test!", t);
        System.exit(-1);
      }
    });
    thread.start();
  }
}
