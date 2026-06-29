package com.rave.projectbabylonmaterials.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rave.projectbabylonmaterials.client.shadow.OutlineOnlyBufferSource;
import com.rave.projectbabylonmaterials.client.shadow.ShadowFormClientState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.RenderEngine;
import yesman.epicfight.client.renderer.patched.layer.PatchedItemInHandLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(value = PatchedItemInHandLayer.class, remap = false)
public abstract class PatchedItemInHandLayerMixin<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends EntityModel<E>> {
    @Inject(method = "renderLayer", at = @At("HEAD"), cancellable = true)
    private void projectBabylonMaterials$renderHeldItemsOutlineOnly(T entitypatch, E entityliving, RenderLayer<E, M> vanillaLayer,
                                                                    PoseStack poseStack, MultiBufferSource buffer,
                                                                    int packedLight, OpenMatrix4f[] poses, float bob, float yRot,
                                                                    float xRot, float partialTicks, CallbackInfo ci) {
        if (!ShadowFormClientState.isConcealed(entityliving)) {
            return;
        }

        OutlineBufferSource outlineBuffer = Minecraft.getInstance().renderBuffers().outlineBufferSource();
        outlineBuffer.setColor(255, 255, 255, 255);
        MultiBufferSource outlineOnlyBuffer = new OutlineOnlyBufferSource(outlineBuffer);
        RenderEngine renderEngine = ClientEngine.getInstance().renderEngine;

        ItemStack mainHandStack = entitypatch.getOriginal().getMainHandItem();
        if (!mainHandStack.isEmpty()) {
            renderEngine.getItemRenderer(mainHandStack).renderItemInHand(mainHandStack, entitypatch, InteractionHand.MAIN_HAND,
                    poses, outlineOnlyBuffer, poseStack, packedLight, partialTicks);
        }

        ItemStack offHandStack = entitypatch.getOriginal().getOffhandItem();
        if (entitypatch.isOffhandItemValid() && !offHandStack.isEmpty()) {
            renderEngine.getItemRenderer(offHandStack).renderItemInHand(offHandStack, entitypatch, InteractionHand.OFF_HAND,
                    poses, outlineOnlyBuffer, poseStack, packedLight, partialTicks);
        }

        ci.cancel();
    }
}