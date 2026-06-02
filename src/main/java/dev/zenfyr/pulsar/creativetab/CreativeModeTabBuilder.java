package dev.zenfyr.pulsar.creativetab;

import java.util.Optional;
import java.util.function.Supplier;
import lombok.NonNull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

/**
 * An alternative creative mode tab builder with pulsar extensions.
 */
public interface CreativeModeTabBuilder {

  static CreativeModeTabBuilder create(@NonNull Identifier identifier) {
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

  /**
   * unlike the vanilla collector, this one allows multiple of the same item.
   */
  CreativeModeTabBuilder entries(PulsarEntries.Collector collector);

  CreativeModeTabBuilder displayName(Component displayName);

  Identifier location();

  @Nullable CreativeModeTab build();

  default Optional<CreativeModeTab> optional() {
    return Optional.ofNullable(build());
  }
}
