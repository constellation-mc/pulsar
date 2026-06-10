package dev.zenfyr.pulsar.test.autotest;

import com.google.common.collect.Iterables;
import dev.zenfyr.pulsar.itemstack.ItemStackUtil;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
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
    var relative = context.relativePos(new BlockPos(0, 1, 0));
    ItemStackUtil.spawn(relative, Items.AMETHYST_SHARD.getDefaultInstance(), context.getLevel());
    var items = Stream.of(Iterables.toArray(context.getLevel().getAllEntities(), Entity.class))
        .filter(entity -> entity.getType() == EntityType.ITEM)
        .filter(Entity::isAlive)
        .filter(entity -> ((ItemEntity) entity).getItem().getItem() == Items.AMETHYST_SHARD)
        .toList();
    context.assertTrue(items.size() == 1, "1 item created using ItemStackUtil.spawn");
    items.forEach(Entity::discard);
    context.succeed();
  }
}
