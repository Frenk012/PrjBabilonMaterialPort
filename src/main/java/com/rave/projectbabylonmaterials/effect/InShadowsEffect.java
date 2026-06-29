package com.rave.projectbabylonmaterials.effect;

import com.rave.projectbabylonmaterials.init.PBMEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InShadowsEffect extends MobEffect {
    private static final float DAMAGE_MULTIPLIER = 1.15F;

    public InShadowsEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x3A3156);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0.0F) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        if (!attacker.hasEffect(PBMEffects.IN_SHADOWS.get())) {
            return;
        }

        event.setAmount(event.getAmount() * DAMAGE_MULTIPLIER);
    }
}