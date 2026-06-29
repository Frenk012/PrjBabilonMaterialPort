package com.rave.projectbabylonmaterials.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FearDebuff extends MobEffect {
    private static final Logger LOGGER = LogManager.getLogger();

    public FearDebuff() {
        super(MobEffectCategory.HARMFUL, 0x8B0000);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide()) {
            entity.setDeltaMovement(
                    entity.getDeltaMovement().x * 0.3,
                    entity.getDeltaMovement().y,
                    entity.getDeltaMovement().z * 0.3
            );
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
