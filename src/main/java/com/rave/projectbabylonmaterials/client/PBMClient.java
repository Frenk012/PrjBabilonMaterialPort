package com.rave.projectbabylonmaterials.client;

import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import com.rave.projectbabylonmaterials.client.gem.GemClientProperties;
import com.rave.projectbabylonmaterials.client.gui.screen.JewelryTableScreen;
import com.rave.projectbabylonmaterials.client.gui.screen.MagicalInfuserScreen;
import com.rave.projectbabylonmaterials.client.gui.screen.RefinementTableScreen;
import com.rave.projectbabylonmaterials.client.gui.screen.ReforgeTableScreen;
import com.rave.projectbabylonmaterials.client.overlay.CombatStatsOverlay;
import com.rave.projectbabylonmaterials.init.PBMItems;
import com.rave.projectbabylonmaterials.init.PBMMenus;
import com.rave.projectbabylonmaterials.item.gem.GemItem;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ProjectBabylonMaterials.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class PBMClient {
    private static final ResourceLocation GEM_VISUAL_PROPERTY = ResourceLocation.fromNamespaceAndPath(ProjectBabylonMaterials.MODID, "gem_visual");

    private PBMClient() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(PBMMenus.MAGICAL_INFUSER_MENU.get(), MagicalInfuserScreen::new);
            MenuScreens.register(PBMMenus.JEWELRY_TABLE_MENU.get(), JewelryTableScreen::new);
            MenuScreens.register(PBMMenus.REFORGE_TABLE_MENU.get(), ReforgeTableScreen::new);
            MenuScreens.register(PBMMenus.REFINEMENT_TABLE_MENU.get(), RefinementTableScreen::new);
            registerGemProperties();
        });
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("combat_stats", CombatStatsOverlay.HUD);
    }

    private static void registerGemProperties() {
        for (RegistryObject<Item> itemObject : PBMItems.ITEMS.getEntries()) {
            Item item = itemObject.get();
            if (item instanceof GemItem) {
                ItemProperties.register(item, GEM_VISUAL_PROPERTY, (stack, level, entity, seed) -> GemClientProperties.getVisualProperty(stack));
            }
        }
    }
}
