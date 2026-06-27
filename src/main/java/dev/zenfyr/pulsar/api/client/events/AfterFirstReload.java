package dev.zenfyr.pulsar.api.client.events;

import dev.zenfyr.pulsar.api.event.Bus;

/**
 * This event fires right after the first successful client reload,
 * at the end of the mojang loading overlay and game load times are sent to the telemetry manager.
 */
public interface AfterFirstReload {

  Bus<AfterFirstReload> EVENT = Bus.create(AfterFirstReload.class, afterFirstReload -> () -> {
    for (AfterFirstReload resourceReload : afterFirstReload) {
      resourceReload.afterFirstReload();
    }
  });

  void afterFirstReload();
}
