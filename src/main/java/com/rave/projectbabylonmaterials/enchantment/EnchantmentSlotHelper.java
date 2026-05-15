package com.rave.projectbabylonmaterials.enchantment;

import com.rave.projectbabylonmaterials.rarity.ItemRarityHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public final class EnchantmentSlotHelper {
    public static final int MAX_ENCHANTMENT_SLOTS = 4;
    public static final int NO_AVAILABLE_ENCHANTMENT_SLOTS_COST = -100;
    public static final int NO_ENCHANTMENT_SLOTS_COST = -101;
    public static final String ENCHANTMENT_SLOT_COUNT_TAG = "PBEnchantSlotCount";

    private static final String ENCHANTMENTS_TAG = "Enchantments";
    private static final String STORED_ENCHANTMENTS_TAG = "StoredEnchantments";

    private EnchantmentSlotHelper() {
    }

    public static boolean supportsEnchantmentSlots(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        return stack.is(Items.BOOK)
                || stack.is(Items.ENCHANTED_BOOK)
                || ItemRarityHelper.supportsRarity(stack)
                || getEnchantmentSlotCount(stack) > 0
                || getEnchantmentCount(stack) > 0;
    }

    public static boolean hasEnchantmentTooltip(ItemStack stack) {
        return supportsEnchantmentSlots(stack) && (getVisibleSlotCount(stack) > 0);
    }

    public static boolean hasAnyEnchantmentSlots(ItemStack stack) {
        return !isLimitedBySlots(stack) || getEnchantmentSlotCount(stack) > 0;
    }

    public static boolean hasAvailableSlot(ItemStack stack) {
        if (!isLimitedBySlots(stack)) {
            return true;
        }

        return getEnchantmentCount(stack) < getEnchantmentSlotCount(stack);
    }

    public static boolean wouldExceedSlotLimit(ItemStack stack) {
        if (!isLimitedBySlots(stack)) {
            return false;
        }

        return getEnchantmentCount(stack) > getEnchantmentSlotCount(stack);
    }

    public static int getEnchantmentSlotCount(ItemStack stack) {
        if (stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK)) {
            return MAX_ENCHANTMENT_SLOTS;
        }

        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(ENCHANTMENT_SLOT_COUNT_TAG, Tag.TAG_INT)) {
            return 0;
        }

        return Math.max(0, Math.min(MAX_ENCHANTMENT_SLOTS, tag.getInt(ENCHANTMENT_SLOT_COUNT_TAG)));
    }

    public static int getVisibleSlotCount(ItemStack stack) {
        return Math.max(getEnchantmentSlotCount(stack), Math.min(MAX_ENCHANTMENT_SLOTS, getEnchantmentCount(stack)));
    }

    public static int getEnchantmentCount(ItemStack stack) {
        return getOrderedEnchantments(stack).size();
    }

    public static List<SlottedEnchantment> getOrderedEnchantments(ItemStack stack) {
        List<SlottedEnchantment> enchantments = new ArrayList<>();
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return enchantments;
        }

        String tagKey = getEnchantmentTagKey(stack);
        if (!tag.contains(tagKey, Tag.TAG_LIST)) {
            return enchantments;
        }

        ListTag listTag = tag.getList(tagKey, Tag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag enchantmentTag = listTag.getCompound(i);
            String enchantmentId = enchantmentTag.getString("id");
            int level = enchantmentTag.getInt("lvl");
            ResourceLocation resourceLocation = ResourceLocation.tryParse(enchantmentId);
            if (resourceLocation == null) {
                continue;
            }

            Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(resourceLocation);
            if (enchantment == null) {
                continue;
            }

            enchantments.add(new SlottedEnchantment(enchantment, level));
        }

        return enchantments;
    }

    public static boolean trimToSlotLimit(ItemStack stack) {
        if (!isLimitedBySlots(stack)) {
            return false;
        }

        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return false;
        }

        String tagKey = getEnchantmentTagKey(stack);
        if (!tag.contains(tagKey, Tag.TAG_LIST)) {
            return false;
        }

        int limit = getEnchantmentSlotCount(stack);
        ListTag enchantmentTags = tag.getList(tagKey, Tag.TAG_COMPOUND);
        if (enchantmentTags.size() <= limit) {
            return false;
        }

        ListTag trimmedTags = new ListTag();
        for (int i = 0; i < limit; i++) {
            trimmedTags.add(enchantmentTags.getCompound(i).copy());
        }

        tag.put(tagKey, trimmedTags);
        return true;
    }

    public static boolean shouldBlockEnchantingTable(ItemStack stack) {
        return ItemRarityHelper.supportsSlottedItem(stack) && getEnchantmentSlotCount(stack) <= 0;
    }

    private static boolean isLimitedBySlots(ItemStack stack) {
        return ItemRarityHelper.supportsRarity(stack) || stack.getTag() != null && stack.getTag().contains(ENCHANTMENT_SLOT_COUNT_TAG, Tag.TAG_INT);
    }

    private static String getEnchantmentTagKey(ItemStack stack) {
        return stack.is(Items.ENCHANTED_BOOK) ? STORED_ENCHANTMENTS_TAG : ENCHANTMENTS_TAG;
    }

    public record SlottedEnchantment(Enchantment enchantment, int level) {
    }
}