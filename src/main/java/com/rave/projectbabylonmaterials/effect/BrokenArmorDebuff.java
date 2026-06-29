package com.rave.projectbabylonmaterials.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class BrokenArmorDebuff extends MobEffect {
    private static final UUID ARMOR_REDUCTION_UUID = UUID.fromString("c28a4c9f-bd02-4d3e-9ab0-f6e1a28ffb5a");

    public BrokenArmorDebuff() {
        super(MobEffectCategory.HARMFUL, 0x6B6B6B);
        addAttributeModifier(Attributes.ARMOR, ARMOR_REDUCTION_UUID.toString(), -0.20D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}

