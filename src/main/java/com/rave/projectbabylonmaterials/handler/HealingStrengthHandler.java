package com.rave.projectbabylonmaterials.handler;

import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import com.rave.projectbabylonmaterials.init.PBAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectBabylonMaterials.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class HealingStrengthHandler {
    private HealingStrengthHandler() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHeal(LivingHealEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0.0F) {
            return;
        }

        LivingEntity livingEntity = event.getEntity();
        double healingStrength = livingEntity.getAttributeValue(PBAttributes.HEALING_STRENGTH.get());
        if (Math.abs(healingStrength - 1.0D) <= 0.0001D) {
            return;
        }

        event.setAmount((float) (event.getAmount() * Math.max(0.0D, healingStrength)));
    }
}