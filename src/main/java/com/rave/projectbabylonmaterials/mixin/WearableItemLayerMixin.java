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
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.patched.layer.WearableItemLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(value = WearableItemLayer.class, remap = false)
public abstract class WearableItemLayerMixin<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends HumanoidModel<E>, AM extends HumanoidMesh> {
    @Inject(method = "renderLayer", at = @At("HEAD"), cancellable = true)
    private void projectBabylonMaterials$hideArmor(T entitypatch, E entityliving, HumanoidArmorLayer<E, M, M> vanillaLayer,
                                                   PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                                   OpenMatrix4f[] poses, float bob, float yRot, float xRot,
                                                   float partialTicks, CallbackInfo ci) {
        if (ShadowFormClientState.isConcealed(entityliving)) {
            ci.cancel();
        }
    }
}