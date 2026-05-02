package dev.zenfyr.pulsar.creativetab;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
class PulsarEntriesImpl implements PulsarEntries {

  private final CreativeModeTab.Output entries;

  public PulsarEntriesImpl(CreativeModeTab.Output entries) {
    this.entries = entries;

    if (entries instanceof CreativeModeTab.ItemDisplayBuilder) {
      ((CreativeModeTab.ItemDisplayBuilder) entries).tabContents = new LinkedList<>();
      ((CreativeModeTab.ItemDisplayBuilder) entries).searchTabContents = new LinkedHashSet<>();
    }
  }

  public void add(ItemStack stack, Visibility visibility) {
    if (entries instanceof CreativeModeTab.ItemDisplayBuilder impl) {
      switch (visibility) {
        case TAB_AND_SEARCH -> {
          impl.tabContents.add(stack);
          impl.searchTabContents.add(stack);
        }
        case TAB -> impl.tabContents.add(stack);
        case SEARCH -> impl.searchTabContents.add(stack);
      }
      return;
    }
    // Let's hope that whatever this is doesn't have dumb restrictions.
    switch (visibility) {
      case TAB_AND_SEARCH -> this.entries.accept(
          stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
      case TAB -> this.entries.accept(stack, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
      case SEARCH -> this.entries.accept(stack, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
    }
  }
}
