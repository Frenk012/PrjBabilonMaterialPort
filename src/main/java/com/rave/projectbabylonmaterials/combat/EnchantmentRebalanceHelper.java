package com.rave.projectbabylonmaterials.combat;

import com.rave.projectbabylonmaterials.init.PBMEnchantments;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public final class EnchantmentRebalanceHelper {
    public static final float SHARPNESS_PER_LEVEL = 0.05F;
    public static final float BANE_PER_LEVEL = 0.12F;
    public static final float SMITE_PER_LEVEL = 0.08F;
    public static final float FIRE_ASPECT_CHANCE_PER_LEVEL = 0.15F;
    public static final float FIRE_ASPECT_CHANCE_CAP = 0.30F;
    public static final int FIRE_ASPECT_SECONDS_PER_LEVEL = 4;
    public static final float PROTECTION_PHYSICAL_REDUCTION_PER_LEVEL = 0.01F;
    public static final float MAGIC_RESISTANCE_REDUCTION_PER_LEVEL = 0.01F;
    public static final float POWER_PER_LEVEL = 0.04F;
    public static final float FLAME_CHANCE = 0.50F;
    public static final float INFINITY_SAVE_CHANCE = 0.30F;
    public static final int MAGIC_RESISTANCE_MAX_LEVEL = 5;

    private EnchantmentRebalanceHelper() {
    }

    public static float getMeleeDamageMultiplier(ItemStack weapon, LivingEntity target) {
        if (weapon.isEmpty() || target == null) {
            return 0.0F;
        }

        int sharpness = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, weapon);
        int smite = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SMITE, weapon);
        int bane = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, weapon);
        MobType mobType = target.getMobType();

        if (mobType == MobType.UNDEAD && smite > 0) {
            return smite * SMITE_PER_LEVEL;
        }

        if (mobType == MobType.ARTHROPOD && bane > 0) {
            return bane * BANE_PER_LEVEL;
        }

        if (sharpness > 0) {
            return sharpness * SHARPNESS_PER_LEVEL;
        }

        return 0.0F;
    }

    public static float getFireAspectChance(ItemStack weapon) {
        if (weapon.isEmpty()) {
            return 0.0F;
        }

        int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, weapon);
        return Math.min(level * FIRE_ASPECT_CHANCE_PER_LEVEL, FIRE_ASPECT_CHANCE_CAP);
    }

    public static int getFireAspectDurationSeconds(ItemStack weapon) {
        if (weapon.isEmpty()) {
            return 0;
        }

        int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, weapon);
        return level * FIRE_ASPECT_SECONDS_PER_LEVEL;
    }

    public static float getPhysicalProtectionReduction(LivingEntity entity, DamageSource source) {
        if (entity == null || source == null || !isPhysicalDamage(source)) {
            return 0.0F;
        }

        int totalLevels = 0;
        for (ItemStack armorPiece : entity.getArmorSlots()) {
            totalLevels += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, armorPiece);
        }

        return totalLevels * PROTECTION_PHYSICAL_REDUCTION_PER_LEVEL;
    }

    public static float getMagicResistanceReduction(LivingEntity entity, DamageSource source) {
        if (entity == null || source == null || !isMagicDamage(source)) {
            return 0.0F;
        }

        int totalLevels = 0;
        for (ItemStack armorPiece : entity.getArmorSlots()) {
            totalLevels += EnchantmentHelper.getItemEnchantmentLevel(PBMEnchantments.MAGIC_RESISTANCE.get(), armorPiece);
        }

        return totalLevels * MAGIC_RESISTANCE_REDUCTION_PER_LEVEL;
    }

    public static boolean isPhysicalDamage(DamageSource source) {
        if (source == null) {
            return false;
        }

        return !source.is(DamageTypeTags.BYPASSES_ARMOR)
                && !source.is(DamageTypeTags.IS_FIRE)
                && !source.is(DamageTypeTags.IS_EXPLOSION)
                && !isMagicDamage(source);
    }

    public static boolean isMagicDamage(DamageSource source) {
        if (source == null) {
            return false;
        }

        return source.is(DamageTypes.MAGIC)
                || source.is(DamageTypes.INDIRECT_MAGIC)
                || ArmorCalculationHelper.isIronsSpellbooksDamage(source);
    }
}
