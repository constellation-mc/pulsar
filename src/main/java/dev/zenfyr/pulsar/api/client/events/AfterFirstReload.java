package dev.zenfyr.pulsar.api.client.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * This event fires right after the first successful client reload,
 * at the end of the mojang loading overlay and game load times are sent to the telemetry manager.
 */
public interface AfterFirstReload {

  Event<AfterFirstReload> EVENT =
      EventFactory.createArrayBacked(AfterFirstReload.class, afterFirstReload -> () -> {
        for (AfterFirstReload resourceReload : afterFirstReload) {
          resourceReload.afterFirstReload();
        }
      });

  void afterFirstReload();
}
