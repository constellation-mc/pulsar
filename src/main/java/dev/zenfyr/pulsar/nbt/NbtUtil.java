package dev.zenfyr.pulsar.nbt;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import lombok.experimental.UtilityClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@UtilityClass
@SuppressWarnings("unused")
public class NbtUtil {

  public record Slotted(ItemStack stack, int slot) {}

  public static final Codec<Slotted> CODEC = Codec.of(
      new Encoder<>() {
        @Override
        public <T> DataResult<T> encode(Slotted input, DynamicOps<T> ops, T prefix) {
          return ItemStack.OPTIONAL_CODEC.encodeStart(ops, input.stack()).flatMap(itemData -> {
            RecordBuilder<T> builder = ops.mapBuilder();
            ops.getMap(itemData)
                .result()
                .ifPresent(
                    map -> map.entries().forEach(e -> builder.add(e.getFirst(), e.getSecond())));
            builder.add("Slot", ops.createByte((byte) input.slot()));
            return builder.build(prefix);
          });
        }
      },
      new Decoder<>() {
        @Override
        public <T> DataResult<Pair<Slotted, T>> decode(DynamicOps<T> ops, T input) {
          DataResult<Byte> slotResult = Codec.BYTE.fieldOf("Slot").codec().parse(ops, input);
          DataResult<ItemStack> stackResult = ItemStack.OPTIONAL_CODEC.parse(ops, input);
          return slotResult.flatMap(slot -> stackResult.map(
              stack -> Pair.of(new Slotted(stack, Byte.toUnsignedInt(slot)), ops.empty())));
        }
      });

  public static @NotNull CompoundTag writeInventoryToTag(
      CompoundTag nbt, @NotNull Container inventory) {
    return writeInventoryToTag("Items", nbt, inventory);
  }

  /**
   * Writes items in a container to {@link CompoundTag}.
   *
   * @param tag       the {@link CompoundTag} to write the container to
   * @param container the container to write to the {@link CompoundTag}
   * @return the {@link CompoundTag} with the container data written to it
   */
  public static @NotNull CompoundTag writeInventoryToTag(
      String key, CompoundTag tag, @NotNull Container container) {
    tag = (tag == null) ? new CompoundTag() : tag;
    ListTag nbtList = new ListTag();
    for (int i = 0; i < container.getContainerSize(); ++i) {
      ItemStack itemStack = container.getItem(i);
      if (!itemStack.isEmpty())
        nbtList.add(
            CODEC.encodeStart(NbtOps.INSTANCE, new Slotted(itemStack, i)).getOrThrow());
    }
    tag.put(key, nbtList);
    return tag;
  }

  public static void readInventoryFromTag(CompoundTag tag, Container inventory) {
    readInventoryFromTag("Items", tag, inventory);
  }

  /**
   * Reads items in a container from a {@link CompoundTag}.
   *
   * @param tag       the {@link CompoundTag} to read the container from
   * @param container the container to read the data into
   */
  public static void readInventoryFromTag(String key, CompoundTag tag, Container container) {
    if (tag == null) return;
    if (!tag.contains(key)) return;

    ListTag nbtList = tag.getListOrEmpty(key);
    for (int i = 0; i < nbtList.size(); ++i) {
      CompoundTag nbtCompound = nbtList.getCompoundOrEmpty(i);
      int j = nbtCompound.getByteOr("Slot", (byte) 0) & 255;
      //noinspection ConstantConditions
      if (j >= 0 && j < container.getContainerSize()) {
        container.setItem(
            j, ItemStack.OPTIONAL_CODEC.parse(NbtOps.INSTANCE, nbtCompound).getOrThrow());
      }
    }
  }

  public static @NotNull ValueOutput writeInventoryToOutput(
      @NotNull ValueOutput nbt, @NotNull Container inventory) {
    return writeInventoryToOutput("Items", nbt, inventory);
  }

  /**
   * Writes items in a container to {@link ValueOutput}.
   *
   * @param tag       the {@link ValueOutput} to write the container to
   * @param container the container to write to the {@link ValueOutput}
   * @return the {@link ValueOutput} with the container data written to it
   */
  public static @NotNull ValueOutput writeInventoryToOutput(
      String key, @NotNull ValueOutput tag, @NotNull Container container) {
    var list = tag.list(key, CODEC);
    for (int i = 0; i < container.getContainerSize(); ++i) {
      ItemStack itemStack = container.getItem(i);
      if (!itemStack.isEmpty()) {
        list.add(new Slotted(itemStack, i));
      }
    }
    return tag;
  }

  public static void readInventoryFromInput(CompoundTag tag, Container inventory) {
    readInventoryFromTag("Items", tag, inventory);
  }

  /**
   * Reads items in a container from a {@link ValueInput}.
   *
   * @param tag       the {@link ValueInput} to read the container from
   * @param container the container to read the data into
   */
  public static void readInventoryFromInput(String key, ValueInput tag, Container container) {
    if (tag == null) return;
    if (!tag.contains(key)) return;

    var nbtList = tag.listOrEmpty(key, CODEC).stream().toList();
    for (Slotted slotted : nbtList) {
      if (slotted.slot() >= 0 && slotted.slot() < container.getContainerSize()) {
        container.setItem(slotted.slot(), slotted.stack());
      }
    }
  }

  @Contract("null, _, _ -> param3")
  public static int getInt(CompoundTag tag, String name, int defaultValue) {
    if (tag == null || !tag.contains(name)) return defaultValue;
    return tag.getIntOr(name, defaultValue);
  }

  @Contract("null, _, _ -> param3")
  public static float getFloat(CompoundTag tag, String name, float defaultValue) {
    if (tag == null || !tag.contains(name)) return defaultValue;
    return tag.getFloatOr(name, defaultValue);
  }

  @Contract("null, _, _ -> param3")
  public static double getDouble(CompoundTag tag, String name, double defaultValue) {
    if (tag == null || !tag.contains(name)) return defaultValue;
    return tag.getDoubleOr(name, defaultValue);
  }

  @Contract("null, _, _ -> param3")
  public static byte getByte(CompoundTag tag, String name, byte defaultValue) {
    if (tag == null || !tag.contains(name)) return defaultValue;
    return tag.getByteOr(name, defaultValue);
  }

  @Contract("null, _, _ -> param3")
  public static String getString(CompoundTag tag, String name, String defaultValue) {
    if (tag == null || !tag.contains(name)) return defaultValue;
    return tag.getStringOr(name, defaultValue);
  }

  @Deprecated
  @Contract("null, _, _, _ -> param3")
  public static int getInt(CompoundTag tag, String name, int min, int max) {
    if (tag == null || !tag.contains(name)) return min;
    int i = tag.getIntOr(name, min);
    return Mth.clamp(i, min, max);
  }

  @Deprecated
  @Contract("null, _, _, _ -> param3")
  public static float getFloat(CompoundTag tag, String name, float min, float max) {
    if (tag == null || !tag.contains(name)) return min;
    float i = tag.getFloatOr(name, min);
    return Mth.clamp(i, min, max);
  }

  @Deprecated
  @Contract("null, _, _, _ -> param3")
  public static double getDouble(CompoundTag tag, String name, double min, double max) {
    if (tag == null || !tag.contains(name)) return min;
    double i = tag.getDoubleOr(name, min);
    return Mth.clamp(i, min, max);
  }

  @Deprecated
  @Contract("null, _, _, _ -> param3")
  public static float getByte(CompoundTag tag, String name, byte min, byte max) {
    if (tag == null || !tag.contains(name)) return min;
    byte i = tag.getByteOr(name, min);
    return Mth.clamp(i, min, max);
  }
}
