package com.rave.projectbabylonmaterials.mixin;

import com.rave.projectbabylonmaterials.client.shadow.ShadowFormClientState;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.particle.AbstractTrailParticle;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;

@Mixin(value = AbstractTrailParticle.class, remap = false)
public abstract class AbstractTrailParticleMixin {
    @Shadow protected EntityPatch<?> owner;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true, require = 0)
    private void pbm$discardTrailForShadowForm(CallbackInfo ci) {
        if (this.owner != null && ShadowFormClientState.isConcealed(this.owner.getOriginal())) {
            ((Particle)(Object)this).remove();
            ci.cancel();
        }
    }
}