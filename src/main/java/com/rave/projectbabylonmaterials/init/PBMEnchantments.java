package com.rave.projectbabylonmaterials.init;

import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import com.rave.projectbabylonmaterials.enchantment.MagicResistanceEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class PBMEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ProjectBabylonMaterials.MODID);

    public static final RegistryObject<Enchantment> MAGIC_RESISTANCE = ENCHANTMENTS.register("magic_resistance",
            MagicResistanceEnchantment::new);

    private PBMEnchantments() {
    }

    public static void register(IEventBus modBus) {
        ENCHANTMENTS.register(modBus);
    }
}