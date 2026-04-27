package com.rave.projectbabylonmaterials.setbonus;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public interface ArmorSetBonus {

    ArmorSetBonusType getType();

    Component getDisplayName();

    ResourceLocation getFrameTexture();

    ResourceLocation getIconTexture();

    void apply(Player player);

    void remove(Player player);

    boolean isApplied(Player player);

    List<Component> getTooltipLines();
}

