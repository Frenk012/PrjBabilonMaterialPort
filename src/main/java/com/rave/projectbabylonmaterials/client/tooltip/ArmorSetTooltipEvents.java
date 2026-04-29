package com.rave.projectbabylonmaterials.client.tooltip;

import com.mojang.datafixers.util.Either;
import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import com.rave.projectbabylonmaterials.setbonus.ArmorPieceTooltipData;
import com.rave.projectbabylonmaterials.setbonus.ArmorSetBonusManager;
import com.rave.projectbabylonmaterials.setbonus.ArmorSetBonusTooltipData;
import com.rave.projectbabylonmaterials.setbonus.ArmorSetTooltipData;
import com.rave.projectbabylonmaterials.tooltip.DescriptionBoxTooltipData;
import com.rave.projectbabylonmaterials.tooltip.IconLabelTooltipData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = ProjectBabylonMaterials.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ArmorSetTooltipEvents {

    private ArmorSetTooltipEvents() {
    }

    @SubscribeEvent
    public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        if (!Screen.hasShiftDown()) {
            return;
        }

        ArmorSetTooltipData data = ArmorSetBonusManager.createTooltipData(event.getItemStack(), Minecraft.getInstance().player);
        if (data == null) {
            return;
        }

        List<Either<net.minecraft.network.chat.FormattedText, net.minecraft.world.inventory.tooltip.TooltipComponent>> elements = event.getTooltipElements();
        int setTooltipStart = elements.size() - ArmorSetBonusManager.getExpandedTooltipLineCount(data);
        if (setTooltipStart < 0) {
            return;
        }

        for (int i = setTooltipStart; i < elements.size(); i++) {
            Either<net.minecraft.network.chat.FormattedText, net.minecraft.world.inventory.tooltip.TooltipComponent> element = elements.get(i);
            if (element.left().isEmpty()) {
                continue;
            }

            String line = element.left().get().getString();
            ArmorSetTooltipData.ArmorPieceEntry piece = findArmorPieceForLine(data, line);
            if (piece != null) {
                elements.set(i, Either.right(new ArmorPieceTooltipData(piece.stack(), piece.label(), piece.equipped())));
                continue;
            }

            ArmorSetTooltipData.BonusEntry bonus = findBonusForLine(data, line);
            if (bonus != null) {
                Component label = bonus.type().getTitle().copy().withStyle(ChatFormatting.GOLD)
                        .append(Component.literal(": ").withStyle(ChatFormatting.GOLD))
                        .append(bonus.displayName().copy().withStyle(ChatFormatting.AQUA));
                elements.set(i, Either.right(new ArmorSetBonusTooltipData(label, bonus.frameTexture(), bonus.iconTexture())));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = ProjectBabylonMaterials.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class ModBusEvents {

        private ModBusEvents() {
        }

        @SubscribeEvent
        public static void onRegisterTooltipFactories(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(ArmorSetTooltipData.class, ArmorSetClientTooltip::new);
            event.register(ArmorPieceTooltipData.class, ArmorPieceClientTooltip::new);
            event.register(ArmorSetBonusTooltipData.class, ArmorSetBonusClientTooltip::new);
            event.register(IconLabelTooltipData.class, IconLabelClientTooltip::new);
            event.register(DescriptionBoxTooltipData.class, DescriptionBoxClientTooltip::new);
        }
    }

    private static ArmorSetTooltipData.ArmorPieceEntry findArmorPieceForLine(ArmorSetTooltipData data, String line) {
        for (ArmorSetTooltipData.ArmorPieceEntry piece : data.armorPieces()) {
            if (line.contains(piece.label().getString())) {
                return piece;
            }
        }
        return null;
    }

    private static ArmorSetTooltipData.BonusEntry findBonusForLine(ArmorSetTooltipData data, String line) {
        for (ArmorSetTooltipData.BonusEntry bonus : data.bonuses()) {
            String title = bonus.type().getTitle().getString() + ":";
            if (line.contains(title) && line.contains(bonus.displayName().getString())) {
                return bonus;
            }
        }
        return null;
    }
}
