package com.rave.projectbabylonmaterials.init;

import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import com.rave.projectbabylonmaterials.effect.AshMemoryEffect;
import com.rave.projectbabylonmaterials.effect.BleedDebuff;
import com.rave.projectbabylonmaterials.effect.BrimstoneFireDebuff;
import com.rave.projectbabylonmaterials.effect.BrimstoneFlamesDebuff;
import com.rave.projectbabylonmaterials.effect.BrokenArmorDebuff;
import com.rave.projectbabylonmaterials.effect.ChainedDebuff;
import com.rave.projectbabylonmaterials.effect.ConcussedDebuff;
import com.rave.projectbabylonmaterials.effect.CritResistanceEffect;
import com.rave.projectbabylonmaterials.effect.ExhaustedDebuff;
import com.rave.projectbabylonmaterials.effect.FearDebuff;
import com.rave.projectbabylonmaterials.effect.FrozenDebuff;
import com.rave.projectbabylonmaterials.effect.HolySigilEffect;
import com.rave.projectbabylonmaterials.effect.InShadowsEffect;
import com.rave.projectbabylonmaterials.effect.MagicBrokenArmorDebuff;
import com.rave.projectbabylonmaterials.effect.MagicalResistanceEffect;
import com.rave.projectbabylonmaterials.effect.MarkedDebuff;
import com.rave.projectbabylonmaterials.effect.PhysicalResistanceEffect;
import com.rave.projectbabylonmaterials.effect.ProvokeDebuff;
import com.rave.projectbabylonmaterials.effect.ShadowFormEffect;
import com.rave.projectbabylonmaterials.effect.UnstableDebuff;
import com.rave.projectbabylonmaterials.effect.WeaponChipDebuff;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class PBMEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ProjectBabylonMaterials.MODID);

    public static final RegistryObject<MobEffect> UNSTABLE = EFFECTS.register("unstable_debuff", UnstableDebuff::new);
    public static final RegistryObject<MobEffect> BLEED_DEBUFF = EFFECTS.register("bleed_debuff", BleedDebuff::new);
    public static final RegistryObject<MobEffect> BROKEN_ARMOR_DEBUFF = EFFECTS.register("broken_armor_debuff", BrokenArmorDebuff::new);
    public static final RegistryObject<MobEffect> WEAPON_CHIP = EFFECTS.register("weapon_chip_debuff", WeaponChipDebuff::new);
    public static final RegistryObject<MobEffect> MAGIC_BROKEN_ARMOR = EFFECTS.register("magic_broken_armor_debuff", MagicBrokenArmorDebuff::new);
    public static final RegistryObject<MobEffect> FEAR_DEBUFF = EFFECTS.register("fear_debuff", FearDebuff::new);
    public static final RegistryObject<MobEffect> MARKED = EFFECTS.register("marked_debuff", MarkedDebuff::new);
    public static final RegistryObject<MobEffect> CHAINED = EFFECTS.register("chained_debuff", ChainedDebuff::new);
    public static final RegistryObject<MobEffect> FROZEN = EFFECTS.register("frozen_debuff", FrozenDebuff::new);
    public static final RegistryObject<MobEffect> BRIMSTONE_FLAMES = EFFECTS.register("brimstone_flames_debuff", BrimstoneFlamesDebuff::new);
    public static final RegistryObject<MobEffect> BRIMSTONE_FIRE = EFFECTS.register("brimstone_fire_debuff", BrimstoneFireDebuff::new);
    public static final RegistryObject<MobEffect> CONCUSSED = EFFECTS.register("concussed_debuff", ConcussedDebuff::new);
    public static final RegistryObject<MobEffect> EXHAUSTED = EFFECTS.register("exhausted_debuff", ExhaustedDebuff::new);
    public static final RegistryObject<MobEffect> PHYSICAL_RESISTANCE = EFFECTS.register("physical_resistance_buff", PhysicalResistanceEffect::new);
    public static final RegistryObject<MobEffect> MAGICAL_RESISTANCE = EFFECTS.register("magical_resistance_buff", MagicalResistanceEffect::new);
    public static final RegistryObject<MobEffect> ASH_MEMORY = EFFECTS.register("ash_memory_buff", AshMemoryEffect::new);
    public static final RegistryObject<MobEffect> HOLY_SIGIL = EFFECTS.register("holy_sigil_buff", HolySigilEffect::new);
    public static final RegistryObject<MobEffect> CRIT_RESISTANCE = EFFECTS.register("crit_resistance_buff", CritResistanceEffect::new);
    public static final RegistryObject<MobEffect> PROVOKE_DEBUFF = EFFECTS.register("provoke_debuff", ProvokeDebuff::new);
    public static final RegistryObject<MobEffect> SHADOW_FORM = EFFECTS.register("shadow_form_buff", ShadowFormEffect::new);
    public static final RegistryObject<MobEffect> IN_SHADOWS = EFFECTS.register("in_shadows_buff", InShadowsEffect::new);

    private PBMEffects() {
    }

    public static void register(IEventBus modBus) {
        EFFECTS.register(modBus);
    }
}