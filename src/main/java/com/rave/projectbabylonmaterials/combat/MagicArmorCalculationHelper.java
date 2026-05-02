package com.rave.projectbabylonmaterials.combat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

public final class MagicArmorCalculationHelper {
    private static final float MAGIC_ARMOR_FULL_AT = 40.0F;
    private static final float MAGIC_ARMOR_DAMAGE_REDUCTION_CAP = 0.50F;
    private static final ResourceLocation SPELL_RESIST_ATTRIBUTE_ID = ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "spell_resist");

    private MagicArmorCalculationHelper() {
    }

    public static float applyAdjustedSpellResist(float spellResistValue, float schoolResistMultiplier) {
        float effectiveMagicArmor = Math.max(0.0F, spellResistValue) * Math.max(0.0F, schoolResistMultiplier);
        float reduction = Math.min(effectiveMagicArmor / MAGIC_ARMOR_FULL_AT, 1.0F) * MAGIC_ARMOR_DAMAGE_REDUCTION_CAP;
        return 1.0F - reduction;
    }

    public static float applyAdjustedMagicDamage(LivingEntity target, float damage) {
        return applyAdjustedMagicDamage(target, damage, 1.0F);
    }

    public static float applyAdjustedMagicDamage(LivingEntity target, float damage, float schoolResistMultiplier) {
        if (damage <= 0.0F) {
            return 0.0F;
        }

        Attribute spellResistAttribute = ForgeRegistries.ATTRIBUTES.getValue(SPELL_RESIST_ATTRIBUTE_ID);
        if (spellResistAttribute == null) {
            return damage;
        }

        float spellResistValue = (float) target.getAttributeValue(spellResistAttribute);
        return damage * applyAdjustedSpellResist(spellResistValue, schoolResistMultiplier);
    }
}
