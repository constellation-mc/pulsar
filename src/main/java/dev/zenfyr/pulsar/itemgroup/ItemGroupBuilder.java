package dev.zenfyr.pulsar.itemgroup;

import dev.zenfyr.pulsar.util.Utilities;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import lombok.NonNull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public interface ItemGroupBuilder {

  static ItemGroupBuilder create(@NonNull ResourceLocation identifier) {
    return new CreativeModeTabBuilderImpl(identifier);
  }

  default ItemGroupBuilder icon(ItemStack itemStack) {
    return this.icon(() -> itemStack);
  }

  default ItemGroupBuilder icon(ItemLike item) {
    return this.icon(new ItemStack(item));
  }

  ItemGroupBuilder icon(Supplier<ItemStack> itemStackSupplier);

  ItemGroupBuilder texture(String texture);

  ItemGroupBuilder entries(PulsarEntries.Collector collector);

  ItemGroupBuilder displayName(Component displayName);

  ItemGroupBuilder register(BooleanSupplier booleanSupplier);

  default ItemGroupBuilder register(boolean bool) {
    return register(bool ? Utilities.getTruth() : Utilities.getFalse());
  }

  ResourceLocation getId();

  @Nullable CreativeModeTab build();

  default Optional<CreativeModeTab> optional() {
    return Optional.ofNullable(build());
  }
}
