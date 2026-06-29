package com.rave.projectbabylonmaterials.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rave.projectbabylonmaterials.client.shadow.OutlineOnlyBufferSource;
import com.rave.projectbabylonmaterials.client.shadow.ShadowFormClientState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin {
    @Invoker("renderArmWithItem")
    protected abstract void projectBabylonMaterials$renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack,
                                                                      ItemDisplayContext displayContext, HumanoidArm arm,
                                                                      PoseStack poseStack, MultiBufferSource bufferSource,
                                                                      int packedLight);

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void pbm$renderHeldItemsOutlineOnly(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                                LivingEntity livingEntity, float limbSwing, float limbSwingAmount,
                                                float partialTick, float ageInTicks, float netHeadYaw,
                                                float headPitch, CallbackInfo ci) {
        if (!ShadowFormClientState.isConcealed(livingEntity)) {
            return;
        }

        OutlineBufferSource outlineBuffer = Minecraft.getInstance().renderBuffers().outlineBufferSource();
        outlineBuffer.setColor(255, 255, 255, 255);
        MultiBufferSource outlineOnlyBuffer = new OutlineOnlyBufferSource(outlineBuffer);

        ItemStack mainHandItem = livingEntity.getMainHandItem();
        ItemStack offHandItem = livingEntity.getOffhandItem();
        if (mainHandItem.isEmpty() && offHandItem.isEmpty()) {
            ci.cancel();
            return;
        }

        HumanoidArm mainArm = livingEntity.getMainArm();
        if (mainArm == HumanoidArm.RIGHT) {
            if (!offHandItem.isEmpty()) {
                this.projectBabylonMaterials$renderArmWithItem(livingEntity, offHandItem, ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                        HumanoidArm.LEFT, poseStack, outlineOnlyBuffer, packedLight);
            }
            if (!mainHandItem.isEmpty()) {
                this.projectBabylonMaterials$renderArmWithItem(livingEntity, mainHandItem, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                        HumanoidArm.RIGHT, poseStack, outlineOnlyBuffer, packedLight);
            }
        } else {
            if (!mainHandItem.isEmpty()) {
                this.projectBabylonMaterials$renderArmWithItem(livingEntity, mainHandItem, ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                        HumanoidArm.LEFT, poseStack, outlineOnlyBuffer, packedLight);
            }
            if (!offHandItem.isEmpty()) {
                this.projectBabylonMaterials$renderArmWithItem(livingEntity, offHandItem, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                        HumanoidArm.RIGHT, poseStack, outlineOnlyBuffer, packedLight);
            }
        }

        ci.cancel();
    }
}