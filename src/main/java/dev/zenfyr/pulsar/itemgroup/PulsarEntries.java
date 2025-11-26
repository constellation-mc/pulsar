package dev.zenfyr.pulsar.itemgroup;

import dev.zenfyr.pulsar.util.MathUtil;
import java.util.Collection;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public interface PulsarEntries {
  void add(ItemStack stack, Visibility visibility);

  default void add(ItemStack stack) {
    this.add(stack, Visibility.TAB_AND_SEARCH);
  }

  default void add(ItemLike item, Visibility visibility) {
    this.add(new ItemStack(item), visibility);
  }

  default void add(ItemLike item) {
    this.add(new ItemStack(item), Visibility.TAB_AND_SEARCH);
  }

  default void addAll(@NotNull Collection<ItemStack> stacks, Visibility visibility) {
    stacks.forEach(stack -> this.add(stack, visibility));
  }

  default void addAll(Collection<ItemStack> stacks) {
    this.addAll(stacks, Visibility.TAB_AND_SEARCH);
  }

  default void appendStacks(Collection<ItemStack> list) {
    appendStacks(list, true);
  }

  default void appendStacks(Collection<ItemStack> list, boolean lineBreak) {
    if (list == null || list.isEmpty())
      return; // we shouldn't add line breaks if there are no items.

    int rows = MathUtil.fastCeil(list.size() / 9d);
    this.addAll(list, Visibility.TAB);
    int left = (rows * 9) - list.size();
    for (int i = 0; i < left; i++) {
      this.add(ItemStack.EMPTY, Visibility.TAB); // fill the gaps
    }
    if (lineBreak)
      this.addAll(NonNullList.withSize(9, ItemStack.EMPTY), Visibility.TAB); // line break
  }

  enum Visibility {
    TAB_AND_SEARCH,
    TAB,
    SEARCH;
  }

  interface Collector {
    void collect(PulsarEntries entries);
  }
}
