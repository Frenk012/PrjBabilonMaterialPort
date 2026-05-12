package com.rave.projectbabylonmaterials.client.tooltip;

import com.mojang.datafixers.util.Either;
import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import com.rave.projectbabylonmaterials.tooltip.EnchantmentDetailsTooltipData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = ProjectBabylonMaterials.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class EnchantmentTooltipEvents {
    private static final Set<String> MODIFIER_HEADERS = Set.of(
            "item.modifiers.mainhand",
            "item.modifiers.offhand",
            "item.modifiers.head",
            "item.modifiers.chest",
            "item.modifiers.legs",
            "item.modifiers.feet"
    );
    private static final Set<String> EPIC_FIGHT_ATTRIBUTE_KEYS = Set.of(
            "attribute.name.epicfight.impact",
            "attribute.name.epicfight.armor_negation",
            "attribute.name.epicfight.max_strikes",
            "attribute.name.epicfight.stun_armor",
            "attribute.name.epicfight.weight",
            "attribute.name.epicfight.staminar",
            "attribute.name.epicfight.stamina_regen",
            "attribute.name.epicfight.execution_resistance",
            "attribute.name.epicfight.offhand_attack_speed",
            "attribute.name.epicfight.offhand_impact",
            "attribute.name.epicfight.offhand_armor_negation",
            "attribute.name.epicfight.offhand_max_strikes"
    );

    private EnchantmentTooltipEvents() {
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if (enchantments.isEmpty()) {
            return;
        }

        List<Enchantment> describedEnchantments = collectDescribedEnchantments(enchantments);
        if (describedEnchantments.isEmpty()) {
            return;
        }

        if (!Screen.hasControlDown()) {
            event.getToolTip().add(Component.translatable("tooltip.project_babylon_materials.hold_ctrl_enchants")
                    .withStyle(ChatFormatting.WHITE));
            return;
        }

        filterTooltipComponents(event.getToolTip(), enchantments);
    }

    @SubscribeEvent
    public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        if (!Screen.hasControlDown()) {
            return;
        }

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(event.getItemStack());
        if (enchantments.isEmpty()) {
            return;
        }

        List<Enchantment> describedEnchantments = collectDescribedEnchantments(enchantments);
        if (describedEnchantments.isEmpty()) {
            return;
        }

        filterTooltipElements(event.getTooltipElements(), enchantments);

        List<EnchantmentDetailsTooltipData.Entry> entries = new ArrayList<>();
        for (Enchantment enchantment : describedEnchantments) {
            entries.add(new EnchantmentDetailsTooltipData.Entry(
                    Component.translatable(enchantment.getDescriptionId()),
                    Component.translatable(getDescriptionKey(enchantment)),
                    createApplicableItems(enchantment)
            ));
        }

        event.getTooltipElements().add(Either.right(new EnchantmentDetailsTooltipData(
                Component.translatable("tooltip.project_babylon_materials.enchantment_descriptions"),
                Component.translatable("tooltip.project_babylon_materials.applies_to"),
                entries
        )));
    }

    private static void filterTooltipComponents(List<Component> tooltip, Map<Enchantment, Integer> enchantments) {
        Set<String> enchantmentLines = buildEnchantmentLineSet(enchantments);
        List<Component> filtered = new ArrayList<>(tooltip.size());
        boolean inModifierSection = false;

        for (Component component : tooltip) {
            String line = component.getString();
            if (isVanillaEnchantmentLine(line, enchantmentLines) || isEpicFightLine(line)) {
                continue;
            }

            if (isModifierHeader(line)) {
                inModifierSection = true;
                continue;
            }

            if (inModifierSection) {
                if (looksLikeModifierLine(line) || isEpicFightLine(line)) {
                    continue;
                }
                inModifierSection = false;
            }

            filtered.add(component);
        }

        tooltip.clear();
        tooltip.addAll(filtered);
    }

    private static void filterTooltipElements(List<Either<FormattedText, TooltipComponent>> elements,
                                              Map<Enchantment, Integer> enchantments) {
        Set<String> enchantmentLines = buildEnchantmentLineSet(enchantments);
        List<Either<FormattedText, TooltipComponent>> filtered = new ArrayList<>(elements.size());
        boolean inModifierSection = false;

        for (Either<FormattedText, TooltipComponent> element : elements) {
            if (element.left().isEmpty()) {
                filtered.add(element);
                continue;
            }

            String line = element.left().get().getString();
            if (isVanillaEnchantmentLine(line, enchantmentLines) || isEpicFightLine(line)) {
                continue;
            }

            if (isModifierHeader(line)) {
                inModifierSection = true;
                continue;
            }

            if (inModifierSection) {
                if (looksLikeModifierLine(line) || isEpicFightLine(line)) {
                    continue;
                }
                inModifierSection = false;
            }

            filtered.add(element);
        }

        elements.clear();
        elements.addAll(filtered);
    }

    private static Set<String> buildEnchantmentLineSet(Map<Enchantment, Integer> enchantments) {
        Set<String> enchantmentLines = new HashSet<>();
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            enchantmentLines.add(entry.getKey().getFullname(entry.getValue()).getString());
        }
        return enchantmentLines;
    }

    private static boolean isVanillaEnchantmentLine(String line, Set<String> enchantmentLines) {
        return enchantmentLines.contains(line);
    }

    private static boolean isModifierHeader(String line) {
        String trimmed = line.trim();
        for (String key : MODIFIER_HEADERS) {
            if (trimmed.equals(I18n.get(key))) {
                return true;
            }
        }
        return false;
    }

    private static boolean looksLikeModifierLine(String line) {
        if (line.isBlank()) {
            return true;
        }
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return true;
        }
        char first = trimmed.charAt(0);
        return first == '+' || first == '-' || Character.isDigit(first);
    }

    private static boolean isEpicFightLine(String line) {
        String trimmed = line.trim();
        if (trimmed.equals(I18n.get("gui.epicfight.attribute"))) {
            return true;
        }

        for (String key : EPIC_FIGHT_ATTRIBUTE_KEYS) {
            String localized = I18n.get(key);
            if (!localized.equals(key) && trimmed.contains(localized)) {
                return true;
            }
        }

        return false;
    }

    private static List<Enchantment> collectDescribedEnchantments(Map<Enchantment, Integer> enchantments) {
        List<Enchantment> describedEnchantments = new ArrayList<>();
        for (Enchantment enchantment : enchantments.keySet()) {
            if (hasDescription(enchantment)) {
                describedEnchantments.add(enchantment);
            }
        }
        describedEnchantments.sort(Comparator.comparing(Enchantment::getDescriptionId));
        return describedEnchantments;
    }

    private static boolean hasDescription(Enchantment enchantment) {
        return I18n.exists(getDescriptionKey(enchantment));
    }

    private static String getDescriptionKey(Enchantment enchantment) {
        return enchantment.getDescriptionId() + ".desc";
    }

    private static List<ItemStack> createApplicableItems(Enchantment enchantment) {
        return switch (enchantment.getDescriptionId()) {
            case "enchantment.minecraft.aqua_affinity", "enchantment.minecraft.respiration" ->
                    List.of(stackOf(Items.IRON_HELMET));
            case "enchantment.minecraft.depth_strider", "enchantment.minecraft.feather_falling",
                    "enchantment.minecraft.frost_walker", "enchantment.minecraft.soul_speed" ->
                    List.of(stackOf(Items.IRON_BOOTS));
            case "enchantment.minecraft.swift_sneak" ->
                    List.of(stackOf(Items.IRON_LEGGINGS));
            case "enchantment.minecraft.protection", "enchantment.minecraft.fire_protection",
                    "enchantment.minecraft.blast_protection", "enchantment.minecraft.projectile_protection",
                    "enchantment.project_babylon_materials.magic_resistance" ->
                    List.of(stackOf(Items.IRON_HELMET), stackOf(Items.IRON_CHESTPLATE), stackOf(Items.IRON_LEGGINGS), stackOf(Items.IRON_BOOTS));
            case "enchantment.minecraft.binding_curse" ->
                    List.of(stackOf(Items.IRON_HELMET), stackOf(Items.IRON_CHESTPLATE), stackOf(Items.IRON_LEGGINGS), stackOf(Items.IRON_BOOTS));
            case "enchantment.minecraft.thorns" ->
                    List.of(stackOf(Items.IRON_CHESTPLATE));
            case "enchantment.minecraft.sharpness", "enchantment.minecraft.smite", "enchantment.minecraft.bane_of_arthropods",
                    "enchantment.minecraft.fire_aspect", "enchantment.minecraft.knockback",
                    "enchantment.minecraft.looting", "enchantment.minecraft.sweeping" ->
                    List.of(stackOf(Items.IRON_SWORD));
            case "enchantment.minecraft.efficiency", "enchantment.minecraft.fortune", "enchantment.minecraft.silk_touch" ->
                    List.of(stackOf(Items.IRON_PICKAXE), stackOf(Items.IRON_AXE), stackOf(Items.IRON_SHOVEL), stackOf(Items.IRON_HOE));
            case "enchantment.minecraft.power", "enchantment.minecraft.flame",
                    "enchantment.minecraft.infinity", "enchantment.minecraft.punch" ->
                    List.of(stackOf(Items.BOW));
            case "enchantment.minecraft.multishot", "enchantment.minecraft.piercing",
                    "enchantment.minecraft.quick_charge" ->
                    List.of(stackOf(Items.CROSSBOW));
            case "enchantment.minecraft.impaling", "enchantment.minecraft.loyalty",
                    "enchantment.minecraft.channeling", "enchantment.minecraft.riptide" ->
                    List.of(stackOf(Items.TRIDENT));
            case "enchantment.minecraft.luck_of_the_sea", "enchantment.minecraft.lure" ->
                    List.of(stackOf(Items.FISHING_ROD));
            case "enchantment.minecraft.mending", "enchantment.minecraft.unbreaking",
                    "enchantment.minecraft.vanishing_curse" ->
                    List.of(stackOf(Items.IRON_SWORD), stackOf(Items.IRON_CHESTPLATE), stackOf(Items.IRON_PICKAXE), stackOf(Items.BOW), stackOf(Items.CROSSBOW), stackOf(Items.TRIDENT));
            default -> List.of(stackOf(Items.ENCHANTED_BOOK));
        };
    }

    private static ItemStack stackOf(net.minecraft.world.item.Item item) {
        return new ItemStack(item);
    }
}
