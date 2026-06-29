package com.rave.projectbabylonmaterials.effect;

import com.rave.projectbabylonmaterials.combat.EnchantmentRebalanceHelper;
import com.rave.projectbabylonmaterials.init.PBMEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshMemoryEffect extends MobEffect {
    private static final float PHYSICAL_DAMAGE_BONUS = 0.10F;
    private static final float IGNITE_CHANCE = 0.50F;
    private static final int IGNITE_SECONDS = 5;

    public AshMemoryEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xC86A2A);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0.0F) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        if (!attacker.hasEffect(PBMEffects.ASH_MEMORY.get())) {
            return;
        }

        if (!EnchantmentRebalanceHelper.isPhysicalDamage(event.getSource())) {
            return;
        }

        event.setAmount(event.getAmount() * (1.0F + PHYSICAL_DAMAGE_BONUS));

        if (event.getSource().getDirectEntity() != attacker) {
            return;
        }

        if (attacker.getMainHandItem().isEmpty()) {
            return;
        }

        if (attacker.getRandom().nextFloat() < IGNITE_CHANCE) {
            event.getEntity().setSecondsOnFire(IGNITE_SECONDS);
        }
    }
}