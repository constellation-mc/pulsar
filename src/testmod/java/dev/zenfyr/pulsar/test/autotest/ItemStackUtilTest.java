package dev.zenfyr.pulsar.test.autotest;

import dev.zenfyr.pulsar.itemstack.ItemStackUtil;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.assertj.core.api.Assertions;

public class ItemStackUtilTest {

  @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
  public void testGetStackOrEmpty(GameTestHelper context) {
    Assertions.assertThat(ItemStackUtil.getStackOrEmpty(Items.GLOW_ITEM_FRAME))
        .extracting(ItemStack::getItem)
        .isEqualTo(Items.GLOW_ITEM_FRAME);

    Assertions.assertThat(ItemStackUtil.getStackOrEmpty(null)).isEqualTo(ItemStack.EMPTY);
    context.succeed();
  }

  @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
  public void testSpawnStack(GameTestHelper context) {
    var relative = new BlockPos(0, 1, 0);
    var absolute = context.absolutePos(relative);
    ItemStackUtil.spawn(absolute, Items.AMETHYST_SHARD.getDefaultInstance(), context.getLevel());

    context.startSequence()
            .thenExecuteAfter(2, () -> context.assertEntityPresent(EntityType.ITEM, relative, 3))
            .thenExecute(context::killAllEntities)
            .thenSucceed();
  }
}
