package com.rave.projectbabylonmaterials.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rave.projectbabylonmaterials.client.shadow.ShadowFormClientState;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void pbm$hideArmorForShadowForm(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                            LivingEntity livingEntity, float limbSwing, float limbSwingAmount,
                                            float partialTick, float ageInTicks, float netHeadYaw,
                                            float headPitch, CallbackInfo ci) {
        if (ShadowFormClientState.isConcealed(livingEntity)) {
            ci.cancel();
        }
    }
}