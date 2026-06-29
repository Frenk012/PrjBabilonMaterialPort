package com.rave.projectbabylonmaterials.effect;

import com.rave.projectbabylonmaterials.init.PBMEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HolySigilEffect extends MobEffect {
    public HolySigilEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xF3E7B2);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0.0F) {
            return;
        }

        if (!event.getEntity().hasEffect(PBMEffects.HOLY_SIGIL.get())) {
            return;
        }

        event.getEntity().removeEffect(PBMEffects.HOLY_SIGIL.get());
        event.setAmount(0.0F);
        event.setCanceled(true);
    }
}