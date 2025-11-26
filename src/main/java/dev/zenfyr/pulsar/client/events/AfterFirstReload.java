package dev.zenfyr.pulsar.client.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface AfterFirstReload {

  Event<AfterFirstReload> EVENT =
      EventFactory.createArrayBacked(AfterFirstReload.class, afterFirstReload -> () -> {
        for (AfterFirstReload resourceReload : afterFirstReload) {
          resourceReload.afterFirstReload();
        }
      });

  void afterFirstReload();
}
