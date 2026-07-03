package dev.zenfyr.pulsar.impl.creativetab;

import dev.zenfyr.pulsar.api.creativetab.CreativeModeTabBuilder;
import dev.zenfyr.pulsar.api.creativetab.PulsarEntries;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.NonNull;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class CreativeModeTabBuilderImpl implements CreativeModeTabBuilder {

  private final Identifier identifier;
  private Supplier<ItemStack> icon = () -> ItemStack.EMPTY;
  private String texture;
  private PulsarEntries.Collector entries;
  private Component displayName;

  public CreativeModeTabBuilderImpl(Identifier identifier) {
    this.identifier = identifier;
  }

  @Override
  public CreativeModeTabBuilder icon(@NonNull Supplier<ItemStack> itemStackSupplier) {
    this.icon = itemStackSupplier;
    return this;
  }

  @Override
  public CreativeModeTabBuilder texture(@NonNull String texture) {
    this.texture = texture;
    return this;
  }

  @Override
  public CreativeModeTabBuilder entries(@NonNull PulsarEntries.Collector collector) {
    this.entries = collector;
    return this;
  }

  @Override
  public CreativeModeTabBuilder displayName(@NonNull Component displayName) {
    this.displayName = displayName;
    return this;
  }

  @Override
  public Identifier identifier() {
    return this.identifier;
  }

  @Override
  public CreativeModeTab build() {
    CreativeModeTab.Builder builder = new CreativeModeTab.Builder(null, -1);
    builder.displayItems((displayContext, operatorEnabled) -> {});
    builder.icon(() -> CreativeModeTabBuilderImpl.this.icon.get());

    builder.title(Objects.requireNonNullElseGet(
        this.displayName,
        () -> Component.translatable("itemGroup." + this.identifier.toString().replace(':', '.'))));
    if (this.texture != null) builder.backgroundTexture(Identifier.parse(this.texture));
    builder.displayItems(
        (displayContext, entries1) -> this.entries.collect(new PulsarEntriesImpl(entries1)));

    CreativeModeTab group = builder.build();
    ((CreativeModeTabDuck) group).pulsar$isPulsarTab(true);
    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, this.identifier, group);
    return group;
  }
}
