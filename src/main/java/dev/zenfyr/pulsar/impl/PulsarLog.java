package dev.zenfyr.pulsar.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum PulsarLog {
  INSTANCE;

  private final Logger logger = LogManager.getLogger("Pulsar");

  public static Logger logger() {
    return INSTANCE.logger;
  }
}
