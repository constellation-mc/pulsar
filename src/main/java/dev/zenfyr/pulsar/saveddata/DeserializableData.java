package dev.zenfyr.pulsar.saveddata;

import net.minecraft.nbt.CompoundTag;

public interface DeserializableData {
  void readSaveData(CompoundTag tag);
}
