package com.rave.projectbabylonmaterials.effect;

import java.util.UUID;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

public class WeaponChipDebuff extends MobEffect {
    private static final UUID IMPACT_REDUCTION_UUID = UUID.fromString("b5ef8de5-4a54-4b5c-80b6-8ef5ff0a3a77");

    public WeaponChipDebuff() {
        super(MobEffectCategory.HARMFUL, 0x7A2D2D);
        addAttributeModifier(EpicFightAttributes.IMPACT.get(), IMPACT_REDUCTION_UUID.toString(), -0.25D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}

