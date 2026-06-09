package dev.zenfyr.pulsar.test.autotest;

import dev.zenfyr.pulsar.creativetab.CreativeModeTabBuilder;
import java.util.List;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import org.assertj.core.api.Assertions;

public class CreativeTabBuilderTest implements ModInitializer {

  public static CreativeModeTab tab;

  @Override
  public void onInitialize() {
    tab = CreativeModeTabBuilder.create(Identifier.fromNamespaceAndPath("pulsar", "test-tab"))
        .entries(entries -> {
          entries.appendStacks(
              List.of(Items.APPLE.getDefaultInstance(), Items.CALCITE.getDefaultInstance()), true);
          entries.appendStacks(
              List.of(Items.APPLE.getDefaultInstance(), Items.CALCITE.getDefaultInstance()), true);

          entries.add(Items.GLOW_ITEM_FRAME);
        })
        .icon(Items.BLUE_ORCHID)
        .build();

    Assertions.assertThat(tab)
        .isNotNull()
        .matches(group -> !group.shouldDisplay(), "tab is special")
        .matches(
            group -> group.getIconItem().getItem() == Items.BLUE_ORCHID, "icon is of 'blue_orchid'")
        .extracting(CreativeModeTab::getDisplayName)
        .isNotNull();
  }
}
