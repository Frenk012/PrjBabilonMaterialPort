package com.rave.projectbabylonmaterials.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class ConcussedDebuff extends MobEffect {
    private static final UUID ATTACK_SPEED_REDUCTION_UUID = UUID.fromString("f4f9c3a6-6c31-4ea0-8f9c-6f0b7c8279aa");
    private static final UUID MOVEMENT_SPEED_REDUCTION_UUID = UUID.fromString("ba5f8d89-f3d5-4f0f-9c1f-b4f9e2f0bce5");
    private static final UUID ATTACK_DAMAGE_REDUCTION_UUID = UUID.fromString("4df3d3ea-2e33-4f61-8825-7adb4a4f5e44");

    public ConcussedDebuff() {
        super(MobEffectCategory.HARMFUL, 0x8A6A4B);
        addAttributeModifier(Attributes.ATTACK_SPEED, ATTACK_SPEED_REDUCTION_UUID.toString(), -0.30D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_REDUCTION_UUID.toString(), -0.30D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_REDUCTION_UUID.toString(), -0.30D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}

