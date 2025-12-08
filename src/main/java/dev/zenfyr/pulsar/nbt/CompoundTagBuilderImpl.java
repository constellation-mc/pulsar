package dev.zenfyr.pulsar.nbt;

import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.apache.commons.lang3.ArrayUtils;

class CompoundTagBuilderImpl implements CompoundTagBuilder {

  private final CompoundTag tag;

  public CompoundTagBuilderImpl(CompoundTag tag) {
    if (tag == null) tag = new CompoundTag();
    this.tag = tag;
  }

  public CompoundTagBuilderImpl() {
    this.tag = new CompoundTag();
  }

  public CompoundTagBuilder put(String key, @NonNull Tag element) {
    tag.put(key, element);
    return this;
  }

  @Override
  public CompoundTagBuilder put(String key, @NonNull CompoundTagBuilder builder) {
    tag.put(key, builder.build());
    return null;
  }

  public CompoundTagBuilder putByte(String key, byte value) {
    tag.putByte(key, value);
    return this;
  }

  public CompoundTagBuilder putShort(String key, short value) {
    tag.putShort(key, value);
    return this;
  }

  public CompoundTagBuilder putInt(String key, int value) {
    tag.putInt(key, value);
    return this;
  }

  public CompoundTagBuilder putLong(String key, long value) {
    tag.putLong(key, value);
    return this;
  }

  public CompoundTagBuilder putUuid(String key, @NonNull UUID uuid) {
    tag.putIntArray(key, UUIDUtil.uuidToIntArray(uuid));
    return this;
  }

  public CompoundTagBuilder putFloat(String key, float value) {
    tag.putFloat(key, value);
    return this;
  }

  public CompoundTagBuilder putDouble(String key, double value) {
    tag.putDouble(key, value);
    return this;
  }

  public CompoundTagBuilder putString(String key, @NonNull String string) {
    tag.putString(key, string);
    return this;
  }

  public CompoundTagBuilder putByteArray(String key, byte @NonNull [] bytes) {
    tag.putByteArray(key, bytes);
    return this;
  }

  public CompoundTagBuilder putByteArray(String key, @NonNull List<Byte> bytes) {
    tag.putByteArray(key, ArrayUtils.toPrimitive(bytes.toArray(new Byte[0])));
    return this;
  }

  public CompoundTagBuilder putIntArray(String key, int @NonNull [] ints) {
    tag.putIntArray(key, ints);
    return this;
  }

  public CompoundTagBuilder putIntArray(String key, @NonNull List<Integer> ints) {
    tag.putIntArray(key, ArrayUtils.toPrimitive(ints.toArray(new Integer[0])));
    return this;
  }

  public CompoundTagBuilder putLongArray(String key, long @NonNull [] longs) {
    tag.putLongArray(key, longs);
    return this;
  }

  public CompoundTagBuilder putLongArray(String key, @NonNull List<Long> longs) {
    tag.putLongArray(key, ArrayUtils.toPrimitive(longs.toArray(new Long[0])));
    return this;
  }

  public CompoundTagBuilder putBoolean(String key, boolean value) {
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
