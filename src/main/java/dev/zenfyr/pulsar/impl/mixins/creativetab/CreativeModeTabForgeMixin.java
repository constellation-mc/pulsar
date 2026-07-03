package dev.zenfyr.pulsar.impl.mixins.creativetab;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.zenfyr.pulsar.impl.creativetab.CreativeModeTabDuck;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabForgeMixin {

  @WrapOperation(
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraftforge/common/ForgeHooks;onCreativeModeTabBuildContents(Lnet/minecraft/world/item/CreativeModeTab;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/CreativeModeTab$DisplayItemsGenerator;Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;Lnet/minecraft/world/item/CreativeModeTab$Output;)V"),
      method = "buildContents",
      require = 0)
  private void pulsar$disableForgeEvent(
      CreativeModeTab tab,
      ResourceKey<CreativeModeTab> tabKey,
      CreativeModeTab.DisplayItemsGenerator originalGenerator,
      CreativeModeTab.ItemDisplayParameters params,
      CreativeModeTab.Output output,
      Operation<Void> original) {
    if (!((CreativeModeTabDuck) tab).pulsar$isPulsarTab()) {
      original.call(tab, tabKey, originalGenerator, params, output);
    } else {
      originalGenerator.accept(params, output);
    }
  }
}
