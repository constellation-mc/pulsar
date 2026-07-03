package dev.zenfyr.pulsar.impl.mixins.creativetab;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.zenfyr.pulsar.impl.creativetab.CreativeModeTabDuck;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabNeoForgeMixin {

  @WrapOperation(
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/neoforged/neoforge/event/EventHooks;onCreativeModeTabBuildContents(Lnet/minecraft/world/item/CreativeModeTab;Lnet/minecraft/world/item/CreativeModeTab$DisplayItemsGenerator;Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;Lnet/minecraft/world/item/CreativeModeTab$Output;)V"),
      method = "buildContents",
      require = 0)
  private void pulsar$disableForgeEvent(
      CreativeModeTab tab,
      CreativeModeTab.DisplayItemsGenerator originalGenerator,
      CreativeModeTab.ItemDisplayParameters params,
      CreativeModeTab.Output output,
      Operation<Void> original) {
    if (!((CreativeModeTabDuck) tab).pulsar$isPulsarTab()) {
      original.call(tab, originalGenerator, params, output);
    } else {
      originalGenerator.accept(params, output);
    }
  }
}
