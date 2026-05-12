package com.rave.projectbabylonmaterials.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record EnchantmentDetailsTooltipData(Component title, Component appliesToLabel,
                                            List<Entry> entries) implements TooltipComponent {
    public EnchantmentDetailsTooltipData {
        entries = List.copyOf(entries);
    }

    public record Entry(Component enchantmentName, Component description, List<ItemStack> applicableItems) {
        public Entry {
            applicableItems = List.copyOf(applicableItems);
        }
    }
}