package com.rave.projectbabylonmaterials.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.config.ClientConfig;

@Pseudo
@Mixin(targets = "io.sedu.mc.parties.api.mod.epicfight.EFHandler", remap = false)
public abstract class SedpartiesEpicFightHandlerMixin {
    @Shadow
    private boolean shaderRender;

    @Inject(method = "setRenderEngine", at = @At("HEAD"), cancellable = true, remap = false)
    private void pbm$useCurrentEpicFightClientConfig(boolean renderUi, CallbackInfo ci) {
        if (renderUi) {
            this.shaderRender = ClientConfig.ACTIVATE_COMPUTE_SHADER.get();
            ClientConfig.ACTIVATE_COMPUTE_SHADER.set(false);
            ClientConfig.activateComputeShader = false;
        } else {
            ClientConfig.ACTIVATE_COMPUTE_SHADER.set(this.shaderRender);
            ClientConfig.activateComputeShader = this.shaderRender;
        }

        ci.cancel();
    }
}
