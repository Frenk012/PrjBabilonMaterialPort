package com.rave.projectbabylonmaterials.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import com.rave.projectbabylonmaterials.menu.JewelryTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class JewelryTableScreen extends AbstractContainerScreen<JewelryTableMenu> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(ProjectBabylonMaterials.MODID, "textures/gui/container/jewerly_table.png");

    public JewelryTableScreen(JewelryTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.titleLabelY = 5;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = this.leftPos;
        int y = this.topPos;
        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}