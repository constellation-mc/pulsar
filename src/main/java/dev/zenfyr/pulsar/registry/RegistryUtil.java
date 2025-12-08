package dev.zenfyr.pulsar.registry;

import com.google.common.base.Suppliers;
import dev.zenfyr.pulsar.util.Utilities;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@UtilityClass
@SuppressWarnings("unused")
public class RegistryUtil {

  private static final Map<Block, BlockEntityType<?>> BLOCK_ENTITY_LOOKUP =
      Utilities.supply(new HashMap<>(), map -> {
        BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(beType -> {
          for (Block block : beType.validBlocks) {
            map.putIfAbsent(block, beType);
          }
        });
      });

  @Contract("null -> null")
  public <T extends BlockEntity> BlockEntityType<T> asBlockEntity(@Nullable Block block) {
    if (block == null) return null;

    var r = BLOCK_ENTITY_LOOKUP.get(block);
    if (r != null) return Utilities.cast(r);

    BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(beType -> {
      for (Block block1 : beType.validBlocks) {
        BLOCK_ENTITY_LOOKUP.putIfAbsent(block1, beType);
      }
    });
    return Utilities.cast(BLOCK_ENTITY_LOOKUP.get(block));
  }

  @Contract("null -> null")
  public <T extends Item> T asItem(@Nullable ItemLike item) {
    return item != null ? Utilities.cast(item.asItem()) : null;
  }

  public <T extends AbstractContainerMenu> Supplier<MenuType<T>> screenHandlerType(
      BiFunction<Integer, Inventory, T> factory) {
    return Suppliers.memoize(() -> new MenuType<>(factory::apply, FeatureFlagSet.of()));
  }

  public <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(
      BiFunction<BlockPos, BlockState, T> factory, Block... blocks) {
    return Suppliers.memoize(() -> new BlockEntityType<>(factory::apply, Set.of(blocks)));
  }
}
