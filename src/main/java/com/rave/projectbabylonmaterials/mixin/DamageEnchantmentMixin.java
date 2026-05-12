package com.rave.projectbabylonmaterials.mixin;

import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageEnchantment.class)
public abstract class DamageEnchantmentMixin {
    @Inject(method = "getDamageBonus", at = @At("HEAD"), cancellable = true)
    private void pbm$removeVanillaFlatDamageBonus(int level, MobType mobType, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(0.0F);
    }
}