package dev.zenfyr.pulsar.resources;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.ServerFunctionLibrary;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.tags.TagManager;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.storage.loot.LootDataManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true)
public final class ReloaderType<T extends PreparableReloadListener> {

  public static final ReloaderType<TagManager> TAGS = create(ResourceReloadListenerKeys.TAGS);
  public static final ReloaderType<RecipeManager> RECIPES =
      create(ResourceReloadListenerKeys.RECIPES);
  public static final ReloaderType<ServerAdvancementManager> ADVANCEMENTS =
      create(ResourceReloadListenerKeys.ADVANCEMENTS);
  public static final ReloaderType<ServerFunctionLibrary> FUNCTIONS =
      create(ResourceReloadListenerKeys.FUNCTIONS);
  public static final ReloaderType<LootDataManager> LOOT_TABLES =
      create(ResourceReloadListenerKeys.LOOT_TABLES);

  private final ResourceLocation identifier;

  @Contract("_ -> new")
  public static <T extends PreparableReloadListener> @NotNull ReloaderType<T> create(
      ResourceLocation identifier) {
    return new ReloaderType<>(identifier);
  }
}
