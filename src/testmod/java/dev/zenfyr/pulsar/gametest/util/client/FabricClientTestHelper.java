/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.zenfyr.pulsar.gametest.util.client;

import dev.zenfyr.pulsar.gametest.util.Utils;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

// Provides thread safe utils for interacting with a running game.
public final class FabricClientTestHelper {

  public static void waitForScreen(Class<? extends Screen> screenClass) {
    waitFor(
        "Screen %s".formatted(screenClass.getName()),
        client -> client.screen != null && client.screen.getClass() == screenClass);
  }

  public static void openPauseScreen() {
    setScreen((client) -> new PauseScreen(true));
    waitForScreen(PauseScreen.class);
  }

  public static void openInventory() {
    setScreen((client) -> new InventoryScreen(Objects.requireNonNull(client.player)));

    boolean creative =
        submitAndWait(client -> Objects.requireNonNull(client.player).isCreative());
    waitForScreen(creative ? CreativeModeInventoryScreen.class : InventoryScreen.class);
  }

  public static void closeScreen() {
    setScreen((client) -> null);
  }

  public static void setScreen(Function<Minecraft, Screen> screenSupplier) {
    submit(client -> {
      client.setScreen(screenSupplier.apply(client));
      return null;
    });
  }

  public static void takeScreenshot(String name) {
    // Allow time for any screens to open
    waitFor(Duration.ofSeconds(1));

    submitAndWait(client -> {
      Screenshot.grab(
          FabricLoader.getInstance().getGameDir().toFile(),
          name + "-" + Utils.STAMP + ".png",
          client.getMainRenderTarget(),
          1,
          (message) -> {});
      return null;
    });
  }

  public static void waitForLevelTicks(long ticks) {
    // Wait for the world to be loaded and get the start ticks
    waitFor(
        "Level load",
        client -> client.level != null && !(client.screen instanceof LevelLoadingScreen),
        Duration.ofMinutes(30));
    final long startTicks = submitAndWait(client -> client.level.getGameTime());
    waitFor(
        "Level load",
        client -> Objects.requireNonNull(client.level).getGameTime() > startTicks + ticks,
        Duration.ofMinutes(10));
  }

  static void waitFor(String what, Predicate<Minecraft> predicate) {
    waitFor(what, predicate, Duration.ofSeconds(10));
  }

  static void waitFor(String what, Predicate<Minecraft> predicate, Duration timeout) {
    final LocalDateTime end = LocalDateTime.now().plus(timeout);

    while (true) {
      boolean result = submitAndWait(predicate::test);

      if (result) {
        break;
      }

      if (LocalDateTime.now().isAfter(end)) {
        throw new RuntimeException("Timed out waiting for " + what);
      }

      waitFor(Duration.ofSeconds(1));
    }
  }

  static void waitFor(Duration duration) {
    try {
      Thread.sleep(duration.toMillis());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  static <T> CompletableFuture<T> submit(Function<Minecraft, T> function) {
    return Minecraft.getInstance().submit(() -> function.apply(Minecraft.getInstance()));
  }

  public static <T> T submitAndWait(Function<Minecraft, T> function) {
    return submit(function).join();
  }
}
