package com.rave.projectbabylonmaterials.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperFireAspectMixin {
    @Inject(method = "getFireAspect", at = @At("HEAD"), cancellable = true)
    private static void pbm$removeVanillaGuaranteedFireAspect(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }
}