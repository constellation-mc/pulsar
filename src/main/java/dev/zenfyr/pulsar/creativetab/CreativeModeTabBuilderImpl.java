package dev.zenfyr.pulsar.creativetab;

import java.util.Objects;
import java.util.function.Supplier;
import lombok.NonNull;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
class CreativeModeTabBuilderImpl implements CreativeModeTabBuilder {

  private final ResourceLocation location;
  private Supplier<ItemStack> icon = () -> ItemStack.EMPTY;
  private String texture;
  private PulsarEntries.Collector entries;
  private Component displayName;

  public CreativeModeTabBuilderImpl(ResourceLocation location) {
    this.location = location;
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
  public ResourceLocation location() {
    return this.location;
  }

  @Override
  public CreativeModeTab build() {
    CreativeModeTab.Builder builder = new CreativeModeTab.Builder(null, -1);
    builder.displayItems((displayContext, operatorEnabled) -> {});
    builder.icon(() -> CreativeModeTabBuilderImpl.this.icon.get());

    builder.title(Objects.requireNonNullElseGet(
        this.displayName,
        () -> Component.translatable("itemGroup." + this.location.toString().replace(':', '.'))));
    if (this.texture != null) builder.backgroundTexture(ResourceLocation.parse(this.texture));
    builder.displayItems(
        (displayContext, entries1) -> this.entries.collect(new PulsarEntriesImpl(entries1)));

    CreativeModeTab group = builder.build();
    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, this.location, group);
    return group;
  }
}
