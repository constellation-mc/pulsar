package dev.zenfyr.pulsar.nbt;

import lombok.experimental.UtilityClass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@UtilityClass
@SuppressWarnings("unused")
public class NbtUtil {

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
      if (!itemStack.isEmpty()) {
        nbtList.add(
            itemStack.save(CompoundTagBuilder.create().putByte("Slot", (byte) i).build()));
      }
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

    ListTag nbtList = tag.getList(key, Tag.TAG_COMPOUND);
    for (int i = 0; i < nbtList.size(); ++i) {
      CompoundTag nbtCompound = nbtList.getCompound(i);
      int j = nbtCompound.getByte("Slot") & 255;
      //noinspection ConstantConditions
      if (j >= 0 && j < container.getContainerSize()) {
        container.setItem(j, ItemStack.of(nbtCompound));
      }
    }
  }

  @Contract("null, _, _ -> param3")
  public static int getInt(CompoundTag tag, String name, int defaultValue) {
    if (tag == null || !tag.contains(name)) return defaultValue;
    return tag.getInt(name);
  }

  @Contract("null, _, _ -> param3")
  public static float getFloat(CompoundTag tag, String name, float defaultValue) {
    if (tag == null || !tag.contains(name)) return defaultValue;
    return tag.getFloat(name);
  }

  @Contract("null, _, _ -> param3")
  public static double getDouble(CompoundTag tag, String name, double defaultValue) {
    if (tag == null || !tag.contains(name)) return defaultValue;
    return tag.getDouble(name);
  }

  @Contract("null, _, _ -> param3")
  public static byte getByte(CompoundTag tag, String name, byte defaultValue) {
    if (tag == null || !tag.contains(name)) return defaultValue;
    return tag.getByte(name);
  }

  @Contract("null, _, _ -> param3")
  public static String getString(CompoundTag tag, String name, String defaultValue) {
    if (tag == null || !tag.contains(name)) return defaultValue;
    return tag.getString(name);
  }

  @Deprecated
  @Contract("null, _, _, _ -> param3")
  public static int getInt(CompoundTag tag, String name, int min, int max) {
    if (tag == null || !tag.contains(name)) return min;
    int i = tag.getInt(name);
    return Mth.clamp(i, min, max);
  }

  @Deprecated
  @Contract("null, _, _, _ -> param3")
  public static float getFloat(CompoundTag tag, String name, float min, float max) {
    if (tag == null || !tag.contains(name)) return min;
    float i = tag.getFloat(name);
    return Mth.clamp(i, min, max);
  }

  @Deprecated
  @Contract("null, _, _, _ -> param3")
  public static double getDouble(CompoundTag tag, String name, double min, double max) {
    if (tag == null || !tag.contains(name)) return min;
    double i = tag.getDouble(name);
    return Mth.clamp(i, min, max);
  }

  @Deprecated
  @Contract("null, _, _, _ -> param3")
  public static float getByte(CompoundTag tag, String name, byte min, byte max) {
    if (tag == null || !tag.contains(name)) return min;
    byte i = tag.getByte(name);
    return Mth.clamp(i, min, max);
  }
}
