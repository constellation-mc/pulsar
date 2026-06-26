package dev.zenfyr.pulsar.gametest.common;

import dev.zenfyr.pulsar.registry.RegistryUtil;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.assertj.core.api.Assertions;

public class RegistryUtilTest implements ModInitializer {
  @Override
  public void onInitialize() {
    Assertions.assertThat(RegistryUtil.asBlockEntity(Blocks.CHEST))
        .isEqualTo(BlockEntityType.CHEST);

    Assertions.assertThat(RegistryUtil.<Item>asItem(Blocks.ACACIA_BUTTON))
        .isEqualTo(Items.ACACIA_BUTTON);
  }
}
