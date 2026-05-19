package com.rave.projectbabylonmaterials.init;

import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import com.rave.projectbabylonmaterials.menu.JewelryTableMenu;
import com.rave.projectbabylonmaterials.menu.MagicalInfuserMenu;
import com.rave.projectbabylonmaterials.menu.RefinementTableMenu;
import com.rave.projectbabylonmaterials.menu.ReforgeTableMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class PBMMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ProjectBabylonMaterials.MODID);

    public static final RegistryObject<MenuType<MagicalInfuserMenu>> MAGICAL_INFUSER_MENU =
            MENUS.register("magical_infuser", () -> IForgeMenuType.create(MagicalInfuserMenu::new));

    public static final RegistryObject<MenuType<JewelryTableMenu>> JEWELRY_TABLE_MENU =
            MENUS.register("jewelry_table", () -> IForgeMenuType.create(JewelryTableMenu::new));

    public static final RegistryObject<MenuType<ReforgeTableMenu>> REFORGE_TABLE_MENU =
            MENUS.register("reforge_table", () -> IForgeMenuType.create(ReforgeTableMenu::new));

    public static final RegistryObject<MenuType<RefinementTableMenu>> REFINEMENT_TABLE_MENU =
            MENUS.register("refinement_table", () -> IForgeMenuType.create(RefinementTableMenu::new));

    private PBMMenus() {
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
