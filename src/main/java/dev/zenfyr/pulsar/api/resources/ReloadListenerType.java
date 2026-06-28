package dev.zenfyr.pulsar.api.resources;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.ServerFunctionLibrary;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true)
public final class ReloadListenerType<T extends PreparableReloadListener> {

  public static final ReloadListenerType<RecipeManager> RECIPES =
      create(Identifier.tryParse("minecraft:recipes"));
  public static final ReloadListenerType<ServerAdvancementManager> ADVANCEMENTS =
      create(Identifier.tryParse("minecraft:advancements"));
  public static final ReloadListenerType<ServerFunctionLibrary> FUNCTIONS =
      create(Identifier.tryParse("minecraft:functions"));

  private final Identifier identifier;

  @Contract("_ -> new")
  public static <T extends PreparableReloadListener> @NotNull ReloadListenerType<T> create(
      Identifier identifier) {
    return new ReloadListenerType<>(identifier);
  }
}
