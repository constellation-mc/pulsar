package dev.zenfyr.pulsar.gametest.util;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;

public class Utils {

  public static final boolean ENABLED = System.getProperty("pulsar.auto-test") != null;
  public static final long STAMP = Instant.now().getLong(ChronoField.INSTANT_SECONDS);

  private static final AtomicBoolean CRASHED = new AtomicBoolean();
  private static Set<CheckEntry> CHECKS = new LinkedHashSet<>();

  public static void runChecks(Logger log) {
    Set<CheckEntry> checks;

    synchronized (Utils.class) {
      checks = CHECKS;
      CHECKS = null;
    }

    for (CheckEntry check : checks) {
      try {
        check.check().run();
        log.info("{}: passed", check.description());
      } catch (Throwable throwable) {
        throw new IllegalStateException(
            "Failed late check: %s".formatted(check.description()), throwable);
      }
    }
  }

  public static synchronized void addLateCheck(String description, Runnable check) {
    if (CHECKS == null) throw new IllegalStateException("Game is shutting down!");
    CHECKS.add(new CheckEntry(description, check));
  }

  public static void markCrashed() {
    CRASHED.set(true);
  }

  record CheckEntry(String description, Runnable check) {}
}
