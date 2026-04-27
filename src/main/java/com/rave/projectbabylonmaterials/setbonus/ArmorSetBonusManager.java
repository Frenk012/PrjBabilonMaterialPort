package com.rave.projectbabylonmaterials.setbonus;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ArmorSetBonusManager {

    private static final Map<UUID, String> ACTIVE_SETS = new HashMap<>();

    private ArmorSetBonusManager() {
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side != LogicalSide.SERVER || event.player.level().isClientSide) {
            return;
        }

        Player player = event.player;
        ArmorSetDefinition currentSet = ArmorSetRegistry.findMatching(player);
        String previousSetId = ACTIVE_SETS.get(player.getUUID());

        if (currentSet == null) {
            if (previousSetId != null) {
                removeSetBonus(player, previousSetId);
                ACTIVE_SETS.remove(player.getUUID());
            }
            return;
        }

        if (!currentSet.getId().equals(previousSetId)) {
            if (previousSetId != null) {
                removeSetBonus(player, previousSetId);
            }

            applySetBonuses(player, currentSet);
            ACTIVE_SETS.put(player.getUUID(), currentSet.getId());
            return;
        }

        if (!areBonusesApplied(player, currentSet)) {
            applySetBonuses(player, currentSet);
        }
    }

    public static void appendCollapsedTooltip(ItemStack stack, List<Component> tooltip) {
        if (findSet(stack) != null) {
            tooltip.add(Component.translatable("tooltip.project_babylon_materials.hold_shift").withStyle(ChatFormatting.GRAY));
        }
    }

    public static void appendTooltip(ItemStack stack, List<Component> tooltip, boolean expanded, Player player) {
        ArmorSetDefinition set = findSet(stack);
        if (set == null) {
            return;
        }

        if (!expanded) {
            appendCollapsedTooltip(stack, tooltip);
            return;
        }

        tooltip.add(Component.translatable("tooltip.project_babylon_materials.set_label", Component.translatable(set.getDisplayNameKey()))
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("tooltip.project_babylon_materials.pieces_label", getMatchedPieces(set, player), 4)
                .withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.project_babylon_materials.armor_pieces").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.empty());
        appendPieceLine(tooltip, "tooltip.project_babylon_materials.piece.helmet", hasPiece(set, player, EquipmentSlot.HEAD));
        appendPieceLine(tooltip, "tooltip.project_babylon_materials.piece.chestplate", hasPiece(set, player, EquipmentSlot.CHEST));
        appendPieceLine(tooltip, "tooltip.project_babylon_materials.piece.leggings", hasPiece(set, player, EquipmentSlot.LEGS));
        appendPieceLine(tooltip, "tooltip.project_babylon_materials.piece.boots", hasPiece(set, player, EquipmentSlot.FEET));

        appendBonusSection(tooltip, set.getMaterialBonuses());
        appendBonusSection(tooltip, set.getClassBonuses());
    }

    public static ArmorSetTooltipData createTooltipData(ItemStack stack, Player player) {
        ArmorSetDefinition set = findSet(stack);
        if (set == null) {
            return null;
        }

        List<ArmorSetTooltipData.ArmorPieceEntry> armorPieces = List.of(
                createArmorPieceEntry(set, player, EquipmentSlot.HEAD, "tooltip.project_babylon_materials.piece.helmet"),
                createArmorPieceEntry(set, player, EquipmentSlot.CHEST, "tooltip.project_babylon_materials.piece.chestplate"),
                createArmorPieceEntry(set, player, EquipmentSlot.LEGS, "tooltip.project_babylon_materials.piece.leggings"),
                createArmorPieceEntry(set, player, EquipmentSlot.FEET, "tooltip.project_babylon_materials.piece.boots")
        );

        List<ArmorSetTooltipData.BonusEntry> bonuses = set.getAllBonuses().stream()
                .map(bonus -> new ArmorSetTooltipData.BonusEntry(
                        bonus.getType(),
                        bonus.getDisplayName(),
                        bonus.getFrameTexture(),
                        bonus.getIconTexture(),
                        bonus.getTooltipLines()
                ))
                .toList();

        return new ArmorSetTooltipData(
                Component.translatable(set.getDisplayNameKey()).withStyle(ChatFormatting.GOLD),
                getMatchedPieces(set, player),
                armorPieces,
                bonuses
        );
    }

    public static int getExpandedTooltipLineCount(ArmorSetTooltipData data) {
        int lineCount = 9;
        for (ArmorSetTooltipData.BonusEntry bonus : data.bonuses()) {
            lineCount += 1 + 1 + bonus.descriptionLines().size();
        }
        return lineCount;
    }

    private static ArmorSetDefinition findSet(ItemStack stack) {
        return ArmorSetRegistry.findContaining(stack.getItem());
    }

    private static void removeSetBonus(Player player, String setId) {
        ArmorSetDefinition set = ArmorSetRegistry.findById(setId);
        if (set != null) {
            for (ArmorSetBonus bonus : set.getAllBonuses()) {
                bonus.remove(player);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        String setId = ACTIVE_SETS.remove(event.getEntity().getUUID());
        if (setId != null) {
            removeSetBonus(event.getEntity(), setId);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        ACTIVE_SETS.remove(event.getOriginal().getUUID());
        ACTIVE_SETS.remove(event.getEntity().getUUID());
    }

    private static void applySetBonuses(Player player, ArmorSetDefinition set) {
        for (ArmorSetBonus bonus : set.getAllBonuses()) {
            bonus.apply(player);
        }
    }

    private static boolean areBonusesApplied(Player player, ArmorSetDefinition set) {
        for (ArmorSetBonus bonus : set.getAllBonuses()) {
            if (!bonus.isApplied(player)) {
                return false;
            }
        }
        return true;
    }

    private static int getMatchedPieces(ArmorSetDefinition set, Player player) {
        return player == null ? 0 : set.countMatchedPieces(player);
    }

    private static boolean hasPiece(ArmorSetDefinition set, Player player, EquipmentSlot slot) {
        return player != null && set.hasPiece(player, slot);
    }

    private static ArmorSetTooltipData.ArmorPieceEntry createArmorPieceEntry(ArmorSetDefinition set, Player player, EquipmentSlot slot,
                                                                             String pieceLabelKey) {
        return new ArmorSetTooltipData.ArmorPieceEntry(
                set.getPieceStack(slot),
                Component.translatable(pieceLabelKey),
                hasPiece(set, player, slot)
        );
    }

    private static void appendPieceLine(List<Component> tooltip, String pieceLabelKey, boolean equipped) {
        ChatFormatting stateColor = equipped ? ChatFormatting.GREEN : ChatFormatting.DARK_GRAY;
        String stateKey = equipped ? "tooltip.project_babylon_materials.equipped" : "tooltip.project_babylon_materials.missing";
        tooltip.add(Component.literal("    ")
                .append(Component.translatable(pieceLabelKey).withStyle(ChatFormatting.GRAY))
                .append(Component.literal(": ").withStyle(ChatFormatting.GRAY))
                .append(Component.translatable(stateKey).withStyle(stateColor)));
    }

    private static void appendBonusSection(List<Component> tooltip, List<ArmorSetBonus> bonuses) {
        for (ArmorSetBonus bonus : bonuses) {
            tooltip.add(Component.empty());
            tooltip.add(Component.literal("    ")
                    .append(bonus.getType().getTitle().copy().withStyle(ChatFormatting.GOLD))
                    .append(Component.literal(": ").withStyle(ChatFormatting.GOLD))
                    .append(bonus.getDisplayName().copy().withStyle(ChatFormatting.AQUA)));
            tooltip.addAll(bonus.getTooltipLines());
        }
    }
}

