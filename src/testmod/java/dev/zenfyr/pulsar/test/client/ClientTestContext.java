package dev.zenfyr.pulsar.test.client;

import dev.zenfyr.pulsar.test.util.TestContext;
import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public record ClientTestContext(Minecraft client) implements TestContext<Minecraft> {

  public void sendCommand(String command) {
    FabricClientTestHelper.submitAndWait(client -> {
      client.player.connection.sendCommand(command);
      return null;
    });
  }

  public void waitForScreen(Class<? extends Screen> screenClass) {
    FabricClientTestHelper.waitForScreen(screenClass);
  }

  public <T, S extends Screen> T executeForScreen(
      Class<S> screenClass, BiFunction<Minecraft, S, T> function) {
    return FabricClientTestHelper.submitAndWait(client -> {
      if (screenClass.isInstance(client.screen)) {
        return function.apply(client, screenClass.cast(client.screen));
      }
      throw new IllegalStateException("Expected: %s, got: %s"
          .formatted(
              screenClass.getName(),
              client.screen != null ? client.screen.getClass().getName() : "null"));
    });
  }

  public void openGameMenu() {
    FabricClientTestHelper.openPauseScreen();
  }

  public void openInventory() {
    FabricClientTestHelper.openInventory();
  }

  public void closeScreen() {
    setScreen((client) -> null);
  }

  public void setScreen(Function<Minecraft, Screen> screenSupplier) {
    FabricClientTestHelper.setScreen(screenSupplier);
  }

  public void takeScreenshot(String name) {
    FabricClientTestHelper.takeScreenshot(name);
  }

  public void waitForLevelTicks(long ticks) {
    FabricClientTestHelper.waitForLevelTicks(ticks);
  }

  @Override
  public void waitFor(String what, Predicate<Minecraft> predicate, Duration timeout) {
    FabricClientTestHelper.waitFor(what, predicate, timeout);
  }

  public <T> T submitAndWait(Function<Minecraft, T> function) {
    return FabricClientTestHelper.submit(function).join();
  }

  @Override
  public Minecraft context() {
    return client();
  }
}
