package dev.zenfyr.pulsar.gametest.common;

import dev.zenfyr.pulsar.api.creativetab.CreativeModeTabBuilder;
import dev.zenfyr.pulsar.api.util.SupportUtil;
import dev.zenfyr.pulsar.gametest.util.Utils;
import java.util.List;
import net.fabricmc.api.EnvType;
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

    Utils.addLateCheck(
        "item tab valid",
        () -> Assertions.assertThat(tab)
            .isNotNull()
            .matches(
                tab -> SupportUtil.environment() != EnvType.CLIENT || tab.shouldDisplay(),
                "tab is special")
            .matches(
                group -> group.getIconItem().getItem() == Items.BLUE_ORCHID,
                "icon is of 'blue_orchid'")
            .extracting(CreativeModeTab::getDisplayName)
            .isNotNull());
  }
}
