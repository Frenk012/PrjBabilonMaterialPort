package com.rave.projectbabylonmaterials.gem;

import com.rave.projectbabylonmaterials.rarity.ItemRarityHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class GemSlotHelper {
    public static final int MAX_GEM_SLOTS = 2;
    public static final String GEM_SLOT_COUNT_TAG = "PBGemSlotCount";

    private static final String SOCKETED_GEMS_TAG = "PBGems";

    private GemSlotHelper() {
    }

    public static boolean supportsGemSlots(ItemStack stack) {
        return ItemRarityHelper.supportsSlottedItem(stack);
    }

    public static boolean hasGemTooltip(ItemStack stack) {
        int slotCount = getGemSlotCount(stack);
        int socketedCount = getSocketedGems(stack).size();
        return (supportsGemSlots(stack) || socketedCount > 0) && (slotCount > 0 || socketedCount > 0);
    }

    public static int getGemSlotCount(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(GEM_SLOT_COUNT_TAG, Tag.TAG_INT)) {
            return 0;
        }

        return Math.max(0, Math.min(MAX_GEM_SLOTS, tag.getInt(GEM_SLOT_COUNT_TAG)));
    }

    public static List<ItemStack> getSocketedGems(ItemStack stack) {
        List<ItemStack> gems = new ArrayList<>();
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(SOCKETED_GEMS_TAG, Tag.TAG_LIST)) {
            return gems;
        }

        ListTag listTag = tag.getList(SOCKETED_GEMS_TAG, Tag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            ItemStack gemStack = ItemStack.of(listTag.getCompound(i));
            if (!gemStack.isEmpty()) {
                gems.add(gemStack);
            }
        }

        return gems;
    }

    public static int getVisibleSlotCount(ItemStack stack) {
        return Math.max(getGemSlotCount(stack), Math.min(MAX_GEM_SLOTS, getSocketedGems(stack).size()));
    }
}