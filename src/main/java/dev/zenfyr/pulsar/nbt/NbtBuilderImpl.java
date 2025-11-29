package dev.zenfyr.pulsar.nbt;

import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

class NbtBuilderImpl implements NbtBuilder {

  private final CompoundTag tag;

  public NbtBuilderImpl(CompoundTag tag) {
    if (tag == null) tag = new CompoundTag();
    this.tag = tag;
  }

  public NbtBuilderImpl() {
    this.tag = new CompoundTag();
  }

  public NbtBuilder put(String key, @NonNull Tag element) {
    tag.put(key, element);
    return this;
  }

  @Override
  public NbtBuilder put(String key, @NonNull NbtBuilder builder) {
    tag.put(key, builder.build());
    return null;
  }

  public NbtBuilder putByte(String key, byte value) {
    tag.putByte(key, value);
    return this;
  }

  public NbtBuilder putShort(String key, short value) {
    tag.putShort(key, value);
    return this;
  }

  public NbtBuilder putInt(String key, int value) {
    tag.putInt(key, value);
    return this;
  }

  public NbtBuilder putLong(String key, long value) {
    tag.putLong(key, value);
    return this;
  }

  public NbtBuilder putUuid(String key, @NonNull UUID uuid) {
    tag.putUUID(key, uuid);
    return this;
  }

  public NbtBuilder putFloat(String key, float value) {
    tag.putFloat(key, value);
    return this;
  }

  public NbtBuilder putDouble(String key, double value) {
    tag.putDouble(key, value);
    return this;
  }

  public NbtBuilder putString(String key, @NonNull String string) {
    tag.putString(key, string);
    return this;
  }

  public NbtBuilder putByteArray(String key, byte @NonNull [] bytes) {
    tag.putByteArray(key, bytes);
    return this;
  }

  public NbtBuilder putByteArray(String key, @NonNull List<Byte> bytes) {
    tag.putByteArray(key, bytes);
    return this;
  }

  public NbtBuilder putIntArray(String key, int @NonNull [] ints) {
    tag.putIntArray(key, ints);
    return this;
  }

  public NbtBuilder putIntArray(String key, @NonNull List<Integer> ints) {
    tag.putIntArray(key, ints);
    return this;
  }

  public NbtBuilder putLongArray(String key, long @NonNull [] longs) {
    tag.putLongArray(key, longs);
    return this;
  }

  public NbtBuilder putLongArray(String key, @NonNull List<Long> longs) {
    tag.putLongArray(key, longs);
    return this;
  }

  public NbtBuilder putBoolean(String key, boolean value) {
    tag.putBoolean(key, value);
    return this;
  }

  @Override
  public String toString() {
    return "NbtBuilder{" + "nbt=" + tag + '}';
  }

  public CompoundTag build() {
    return tag;
  }
}
