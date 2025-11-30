package dev.zenfyr.pulsar.itemgroup;

import java.util.Optional;
import java.util.function.Supplier;
import lombok.NonNull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public interface CreativeModeTabBuilder {

  static CreativeModeTabBuilder create(@NonNull ResourceLocation identifier) {
    return new CreativeModeTabBuilderImpl(identifier);
  }

  default CreativeModeTabBuilder icon(ItemStack itemStack) {
    return this.icon(() -> itemStack);
  }

  default CreativeModeTabBuilder icon(ItemLike item) {
    return this.icon(new ItemStack(item));
  }

  CreativeModeTabBuilder icon(Supplier<ItemStack> itemStackSupplier);

  CreativeModeTabBuilder texture(String texture);

  CreativeModeTabBuilder entries(PulsarEntries.Collector collector);

  CreativeModeTabBuilder displayName(Component displayName);

  ResourceLocation location();

  @Nullable CreativeModeTab build();

  default Optional<CreativeModeTab> optional() {
    return Optional.ofNullable(build());
  }
}
