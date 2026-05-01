package com.rave.projectbabylonmaterials.client.overlay;

import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import com.rave.projectbabylonmaterials.config.PBMClientConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectBabylonMaterials.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CombatStatsOverlayEvents {
    private CombatStatsOverlayEvents() {
    }

    @SubscribeEvent
    public static void onRenderGuiOverlayPre(RenderGuiOverlayEvent.Pre event) {
        if (!PBMClientConfig.hideVanillaArmorHud()) {
            return;
        }
        if (event.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type()) {
            event.setCanceled(true);
        }
    }
}