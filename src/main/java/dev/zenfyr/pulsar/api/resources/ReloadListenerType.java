package dev.zenfyr.pulsar.api.resources;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.minecraft.resources.ResourceLocation;
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
      create(ResourceReloadListenerKeys.RECIPES);
  public static final ReloadListenerType<ServerAdvancementManager> ADVANCEMENTS =
      create(ResourceReloadListenerKeys.ADVANCEMENTS);
  public static final ReloadListenerType<ServerFunctionLibrary> FUNCTIONS =
      create(ResourceReloadListenerKeys.FUNCTIONS);

  private final ResourceLocation location;

  @Contract("_ -> new")
  public static <T extends PreparableReloadListener> @NotNull ReloadListenerType<T> create(
      ResourceLocation identifier) {
    return new ReloadListenerType<>(identifier);
  }
}
