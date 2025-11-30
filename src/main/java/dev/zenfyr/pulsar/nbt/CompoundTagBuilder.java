package dev.zenfyr.pulsar.nbt;

import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A quick builder to make NBTs in one line.
 *
 * <p>This class allows you to easily create and modify NBT compounds.
 * You can create a new {@link CompoundTagBuilder} instance by calling the
 * {@link #create()} method, and then use its various put methods
 * to add elements to the NBT compound. You can also use the
 * {@link #create(CompoundTag)} method to start with an existing
 * NBT compound and modify it.
 *
 * <p>Once you are done adding elements to the NBT compound, you can
 * call the {@link #build()} method to obtain the resulting
 * {@link CompoundTag} instance.
 *
 * <p>Here's an example of how to use this class:
 *
 * <pre>
 * {@code
 * NbtCompound myNbt = NbtBuilder.create()
 *     .putInt("myInt", 42)
 *     .putString("myString", "Hello, world!")
 *     .build();
 * }
 * </pre>
 */
@SuppressWarnings("unused")
public interface CompoundTagBuilder {

  @Contract(value = " -> new", pure = true)
  static @NotNull CompoundTagBuilder create() {
    return new CompoundTagBuilderImpl();
  }

  @Contract("_ -> new")
  static @NotNull CompoundTagBuilder create(@Nullable CompoundTag nbt) {
    return new CompoundTagBuilderImpl(nbt);
  }

  CompoundTagBuilder put(String key, Tag element);

  CompoundTagBuilder put(String key, CompoundTagBuilder builder);

  CompoundTagBuilder putByte(String key, byte value);

  CompoundTagBuilder putShort(String key, short value);

  CompoundTagBuilder putInt(String key, int value);

  CompoundTagBuilder putLong(String key, long value);

  CompoundTagBuilder putUuid(String key, UUID value);

  CompoundTagBuilder putFloat(String key, float value);

  CompoundTagBuilder putDouble(String key, double value);

  CompoundTagBuilder putString(String key, String value);

  CompoundTagBuilder putByteArray(String key, byte[] value);

  CompoundTagBuilder putByteArray(String key, List<Byte> value);

  CompoundTagBuilder putIntArray(String key, int[] value);

  CompoundTagBuilder putIntArray(String key, List<Integer> value);

  CompoundTagBuilder putLongArray(String key, long[] value);

  CompoundTagBuilder putLongArray(String key, List<Long> value);

  CompoundTagBuilder putBoolean(String key, boolean value);

  CompoundTag build();
}
