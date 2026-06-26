package dev.zenfyr.pulsar.api.saveddata;

import net.minecraft.nbt.CompoundTag;

public interface DeserializableData {
  void readSaveData(CompoundTag tag);
}
