package dev.zenfyr.pulsar.itemstack;

import dev.zenfyr.pulsar.util.MathUtil;
import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@UtilityClass
@SuppressWarnings("unused")
public class ItemStackUtil {

  public static ItemStack getStackOrEmpty(ItemLike item) {
    return Optional.ofNullable(item)
        .map(ItemLike::asItem)
        .map(Item::getDefaultInstance)
        .orElse(ItemStack.EMPTY);
  }

  public static void spawn(@NonNull BlockPos pos, @NonNull ItemStack stack, @NonNull Level world) {
    ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
    itemEntity.setDefaultPickUpDelay();
    world.addFreshEntity(itemEntity);
  }

  public static void spawn(@NonNull Vec3 pos, @NonNull ItemStack stack, @NonNull Level world) {
    ItemEntity itemEntity = new ItemEntity(world, pos.x(), pos.y(), pos.z(), stack);
    itemEntity.setDefaultPickUpDelay();
    world.addFreshEntity(itemEntity);
  }

  public static void spawnVelocity(
      @NonNull BlockPos pos,
      @NonNull ItemStack stack,
      @NonNull Level world,
      double minX,
      double maxX,
      double minY,
      double maxY,
      double minZ,
      double maxZ) {
    ItemEntity itemEntity = new ItemEntity(
        world,
        pos.getX(),
        pos.getY(),
        pos.getZ(),
        stack,
        MathUtil.nextDouble(minX, maxX),
        MathUtil.nextDouble(minY, maxY),
        MathUtil.nextDouble(minZ, maxZ));
    itemEntity.setDefaultPickUpDelay();
    world.addFreshEntity(itemEntity);
  }

  public static void spawnVelocity(
      @NonNull Vec3 pos,
      @NonNull ItemStack stack,
      @NonNull Level world,
      double minX,
      double maxX,
      double minY,
      double maxY,
      double minZ,
      double maxZ) {
    ItemEntity itemEntity = new ItemEntity(
        world,
        pos.x(),
        pos.y(),
        pos.z(),
        stack,
        MathUtil.nextDouble(minX, maxX),
        MathUtil.nextDouble(minY, maxY),
        MathUtil.nextDouble(minZ, maxZ));
    itemEntity.setDefaultPickUpDelay();
    world.addFreshEntity(itemEntity);
  }

  public static void spawnVelocity(
      @NotNull BlockPos pos, @NonNull ItemStack stack, @NonNull Level world, @NotNull Vec3 vec3d) {
    ItemEntity itemEntity =
        new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack, vec3d.x, vec3d.y, vec3d.z);
    itemEntity.setDefaultPickUpDelay();
    world.addFreshEntity(itemEntity);
  }

  public static void spawnVelocity(
      @NotNull Vec3 pos, @NonNull ItemStack stack, @NonNull Level world, @NotNull Vec3 vec3d) {
    ItemEntity itemEntity =
        new ItemEntity(world, pos.x(), pos.y(), pos.z(), stack, vec3d.x, vec3d.y, vec3d.z);
    itemEntity.setDefaultPickUpDelay();
    world.addFreshEntity(itemEntity);
  }

  public static void appendStacks(Collection<ItemStack> stacks, Collection<ItemStack> list) {
    appendStacks(stacks, list, true);
  }

  public static void appendStacks(
      Collection<ItemStack> stacks, Collection<ItemStack> list, boolean lineBreak) {
    if (list == null || list.isEmpty())
      return; // we shouldn't add line breaks if there are no items.

    int rows = MathUtil.fastCeil(list.size() / 9d);
    stacks.addAll(list);
    int left = (rows * 9) - list.size();
    for (int i = 0; i < left; i++) {
      stacks.add(ItemStack.EMPTY); // fill the gaps
    }
    if (lineBreak) stacks.addAll(NonNullList.withSize(9, ItemStack.EMPTY)); // line break
  }
}
