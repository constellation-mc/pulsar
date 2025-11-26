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

  public static @NotNull CompoundTag writeInventoryToNbt(
      CompoundTag nbt, @NotNull Container inventory) {
    return writeInventoryToNbt("Items", nbt, inventory);
  }

  /**
   * Writes items in an inventory to NbtCompound.
   *
   * @param nbt       the NbtCompound to write the inventory to
   * @param inventory the inventory to write to the NbtCompound
   * @return the NbtCompound with the inventory data written to it
   */
  public static @NotNull CompoundTag writeInventoryToNbt(
      String key, CompoundTag nbt, @NotNull Container inventory) {
    nbt = (nbt == null) ? new CompoundTag() : nbt;
    ListTag nbtList = new ListTag();
    for (int i = 0; i < inventory.getContainerSize(); ++i) {
      ItemStack itemStack = inventory.getItem(i);
      if (!itemStack.isEmpty()) {
        nbtList.add(itemStack.save(NbtBuilder.create().putByte("Slot", (byte) i).build()));
      }
    }
    nbt.put(key, nbtList);
    return nbt;
  }

  public static void readInventoryFromNbt(CompoundTag nbt, Container inventory) {
    readInventoryFromNbt("Items", nbt, inventory);
  }

  /**
   * Reads items in an inventory from a NbtCompound.
   *
   * @param nbt       the NbtCompound to read the inventory from
   * @param inventory the inventory to read the data into
   */
  public static void readInventoryFromNbt(String key, CompoundTag nbt, Container inventory) {
    if (nbt == null) return;
    if (!nbt.contains(key)) return;

    ListTag nbtList = nbt.getList(key, Tag.TAG_COMPOUND);
    for (int i = 0; i < nbtList.size(); ++i) {
      CompoundTag nbtCompound = nbtList.getCompound(i);
      int j = nbtCompound.getByte("Slot") & 255;
      //noinspection ConstantConditions
      if (j >= 0 && j < inventory.getContainerSize()) {
        inventory.setItem(j, ItemStack.of(nbtCompound));
      }
    }
  }

  @Contract("null, _, _ -> param3")
  public static int getInt(CompoundTag nbt, String name, int defaultValue) {
    if (nbt == null || !nbt.contains(name)) return defaultValue;
    return nbt.getInt(name);
  }

  @Contract("null, _, _ -> param3")
  public static float getFloat(CompoundTag nbt, String name, float defaultValue) {
    if (nbt == null || !nbt.contains(name)) return defaultValue;
    return nbt.getFloat(name);
  }

  @Contract("null, _, _ -> param3")
  public static double getDouble(CompoundTag nbt, String name, double defaultValue) {
    if (nbt == null || !nbt.contains(name)) return defaultValue;
    return nbt.getDouble(name);
  }

  @Contract("null, _, _ -> param3")
  public static byte getByte(CompoundTag nbt, String name, byte defaultValue) {
    if (nbt == null || !nbt.contains(name)) return defaultValue;
    return nbt.getByte(name);
  }

  @Contract("null, _, _ -> param3")
  public static String getString(CompoundTag nbt, String name, String defaultValue) {
    if (nbt == null || !nbt.contains(name)) return defaultValue;
    return nbt.getString(name);
  }

  @Deprecated
  @Contract("null, _, _, _ -> param3")
  public static int getInt(CompoundTag nbt, String name, int min, int max) {
    if (nbt == null || !nbt.contains(name)) return min;
    int i = nbt.getInt(name);
    return Mth.clamp(i, min, max);
  }

  @Deprecated
  @Contract("null, _, _, _ -> param3")
  public static float getFloat(CompoundTag nbt, String name, float min, float max) {
    if (nbt == null || !nbt.contains(name)) return min;
    float i = nbt.getFloat(name);
    return Mth.clamp(i, min, max);
  }

  @Deprecated
  @Contract("null, _, _, _ -> param3")
  public static double getDouble(CompoundTag nbt, String name, double min, double max) {
    if (nbt == null || !nbt.contains(name)) return min;
    double i = nbt.getDouble(name);
    return Mth.clamp(i, min, max);
  }

  @Deprecated
  @Contract("null, _, _, _ -> param3")
  public static float getByte(CompoundTag nbt, String name, byte min, byte max) {
    if (nbt == null || !nbt.contains(name)) return min;
    byte i = nbt.getByte(name);
    return Mth.clamp(i, min, max);
  }
}
