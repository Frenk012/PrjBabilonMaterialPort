package com.rave.projectbabylonmaterials.init;
import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import com.rave.projectbabylonmaterials.effect.UnstableEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public final class PBMEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ProjectBabylonMaterials.MODID);
    public static final RegistryObject<MobEffect> UNSTABLE = EFFECTS.register("unstable", UnstableEffect::new);
    private PBMEffects() {
    }
    public static void register(IEventBus modBus) {
        EFFECTS.register(modBus);
    }
}