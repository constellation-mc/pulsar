package dev.zenfyr.pulsar.itemgroup;

import dev.zenfyr.pulsar.util.Utilities;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import lombok.NonNull;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

class ItemGroupBuilderImpl implements ItemGroupBuilder {

  private final ResourceLocation identifier;
  private Supplier<ItemStack> icon = () -> ItemStack.EMPTY;
  private String texture;
  private PulsarEntries.Collector entries;
  private BooleanSupplier register = Utilities.getTruth();
  private Component displayName;

  public ItemGroupBuilderImpl(ResourceLocation identifier) {
    this.identifier = identifier;
  }

  @Override
  public ItemGroupBuilder icon(@NonNull Supplier<ItemStack> itemStackSupplier) {
    this.icon = itemStackSupplier;
    return this;
  }

  @Override
  public ItemGroupBuilder texture(@NonNull String texture) {
    this.texture = texture;
    return this;
  }

  @Override
  public ItemGroupBuilder entries(@NonNull PulsarEntries.Collector collector) {
    this.entries = collector;
    return this;
  }

  @Override
  public ItemGroupBuilder displayName(@NonNull Component displayName) {
    this.displayName = displayName;
    return this;
  }

  @Override
  public ItemGroupBuilder register(@NonNull BooleanSupplier booleanSupplier) {
    this.register = booleanSupplier;
    return this;
  }

  @Override
  public ResourceLocation getId() {
    return this.identifier;
  }

  @Override
  public CreativeModeTab build() {
    if (!this.register.getAsBoolean()) return null;

    CreativeModeTab.Builder builder = new CreativeModeTab.Builder(null, -1);
    builder.displayItems((displayContext, operatorEnabled) -> {});
    builder.icon(() -> ItemGroupBuilderImpl.this.icon.get());

    builder.title(Objects.requireNonNullElseGet(
        this.displayName,
        () -> Component.translatable("itemGroup." + this.identifier.toString().replace(':', '.'))));
    if (this.texture != null) builder.backgroundSuffix(this.texture);
    builder.displayItems(
        (displayContext, entries1) -> this.entries.collect(new PulsarEntriesImpl(entries1)));

    CreativeModeTab group = builder.build();
    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, this.identifier, group);
    return group;
  }
}
