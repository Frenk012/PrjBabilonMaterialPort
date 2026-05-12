package com.rave.projectbabylonmaterials.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;

@Mixin(ProtectionEnchantment.class)
public abstract class ProtectionEnchantmentMixin {
    @Inject(method = "getDamageProtection", at = @At("HEAD"), cancellable = true)
    private void pbm$removeVanillaAllProtection(int level, DamageSource source, CallbackInfoReturnable<Integer> cir) {
        if (pbm$getType() == ProtectionEnchantment.Type.ALL) {
            cir.setReturnValue(0);
        }
    }

    @Unique
    private ProtectionEnchantment.Type pbm$getType() {
        try {
            for (Field field : ProtectionEnchantment.class.getDeclaredFields()) {
                if (field.getType() == ProtectionEnchantment.Type.class) {
                    field.setAccessible(true);
                    Object value = field.get(this);
                    if (value instanceof ProtectionEnchantment.Type type) {
                        return type;
                    }
                }
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }
}