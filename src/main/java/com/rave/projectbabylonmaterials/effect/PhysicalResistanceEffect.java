package com.rave.projectbabylonmaterials.effect;

import com.rave.projectbabylonmaterials.combat.EnchantmentRebalanceHelper;
import com.rave.projectbabylonmaterials.init.PBMEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PhysicalResistanceEffect extends MobEffect {
    public static final float DAMAGE_REDUCTION = 0.10F;

    public PhysicalResistanceEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x7AA6B8);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0.0F) {
            return;
        }

        if (!event.getEntity().hasEffect(PBMEffects.PHYSICAL_RESISTANCE.get())) {
            return;
        }

        if (!EnchantmentRebalanceHelper.isPhysicalDamage(event.getSource())) {
            return;
        }

        event.setAmount(event.getAmount() * (1.0F - DAMAGE_REDUCTION));
    }
}