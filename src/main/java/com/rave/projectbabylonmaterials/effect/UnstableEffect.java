package com.rave.projectbabylonmaterials.effect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.UUID;
public class UnstableEffect extends MobEffect {
    private static final ResourceLocation SPELL_RESIST_ATTRIBUTE_ID = ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "spell_resist");
    private static final UUID SPELL_RESIST_REDUCTION_UUID = UUID.fromString("f41bbbc7-b66e-4f26-9f1d-d7c9d08684a3");
    private static final double SPELL_RESIST_REDUCTION_PER_LEVEL = -0.15D;
    public UnstableEffect() {
        super(MobEffectCategory.HARMFUL, 0xD6AF2C);
    }
    @Override
    public void addAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(livingEntity, attributeMap, amplifier);
        applySpellResistReduction(livingEntity, amplifier);
    }
    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        removeSpellResistReduction(livingEntity);
        super.removeAttributeModifiers(livingEntity, attributeMap, amplifier);
    }
    private static void applySpellResistReduction(LivingEntity livingEntity, int amplifier) {
        AttributeInstance spellResist = resolveSpellResistAttribute(livingEntity);
        if (spellResist == null) {
            return;
        }
        if (spellResist.getModifier(SPELL_RESIST_REDUCTION_UUID) != null) {
            spellResist.removeModifier(SPELL_RESIST_REDUCTION_UUID);
        }
        spellResist.addTransientModifier(new AttributeModifier(
                SPELL_RESIST_REDUCTION_UUID,
                "Unstable spell resist reduction",
                SPELL_RESIST_REDUCTION_PER_LEVEL * (amplifier + 1),
                AttributeModifier.Operation.MULTIPLY_TOTAL
        ));
    }
    private static void removeSpellResistReduction(LivingEntity livingEntity) {
        AttributeInstance spellResist = resolveSpellResistAttribute(livingEntity);
        if (spellResist != null && spellResist.getModifier(SPELL_RESIST_REDUCTION_UUID) != null) {
            spellResist.removeModifier(SPELL_RESIST_REDUCTION_UUID);
        }
    }
    private static AttributeInstance resolveSpellResistAttribute(LivingEntity livingEntity) {
        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(SPELL_RESIST_ATTRIBUTE_ID);
        if (attribute == null) {
            return null;
        }
        return livingEntity.getAttribute(attribute);
    }
}