package dev.zenfyr.pulsar.resources;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
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
public final class ReloaderType<T extends PreparableReloadListener> {

  public static final ReloaderType<RecipeManager> RECIPES =
      create(ResourceReloaderKeys.Server.RECIPES);
  public static final ReloaderType<ServerAdvancementManager> ADVANCEMENTS =
      create(ResourceReloaderKeys.Server.ADVANCEMENTS);
  public static final ReloaderType<ServerFunctionLibrary> FUNCTIONS =
      create(ResourceReloaderKeys.Server.FUNCTIONS);

  private final Identifier location;

  @Contract("_ -> new")
  public static <T extends PreparableReloadListener> @NotNull ReloaderType<T> create(
      Identifier identifier) {
    return new ReloaderType<>(identifier);
  }
}
