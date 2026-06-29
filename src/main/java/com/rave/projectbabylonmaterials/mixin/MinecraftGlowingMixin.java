package com.rave.projectbabylonmaterials.mixin;

import com.rave.projectbabylonmaterials.client.shadow.ShadowFormClientState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftGlowingMixin {
    @Inject(method = "shouldEntityAppearGlowing(Lnet/minecraft/world/entity/Entity;)Z", at = @At("RETURN"), cancellable = true)
    private void pbm$forceShadowFormOutline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (ShadowFormClientState.isConcealed(entity)) {
            cir.setReturnValue(true);
        }
    }
}