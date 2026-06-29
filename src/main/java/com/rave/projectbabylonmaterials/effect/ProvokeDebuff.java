package com.rave.projectbabylonmaterials.effect;

import com.rave.projectbabylonmaterials.init.PBMEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ProvokeDebuff extends MobEffect {
    private static final String PROVOKER_ENTITY_ID_KEY = "project_babylon_materials.provoke_applier_id";
    private static final float DAMAGE_REDUCTION = 0.15F;

    public ProvokeDebuff() {
        super(MobEffectCategory.HARMFUL, 0xB54848);
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect() != PBMEffects.PROVOKE_DEBUFF.get()) {
            return;
        }

        LivingEntity entity = event.getEntity();
        LivingEntity applier = entity.getLastHurtByMob();
        if (applier == null) {
            return;
        }

        entity.getPersistentData().putInt(PROVOKER_ENTITY_ID_KEY, applier.getId());
        if (entity instanceof Mob mob) {
            mob.setTarget(applier);
        }
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        if (event.getEffect() == PBMEffects.PROVOKE_DEBUFF.get()) {
            event.getEntity().getPersistentData().remove(PROVOKER_ENTITY_ID_KEY);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0.0F) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        if (!attacker.hasEffect(PBMEffects.PROVOKE_DEBUFF.get())) {
            return;
        }

        event.setAmount(event.getAmount() * (1.0F - DAMAGE_REDUCTION));
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide() || !(event.getEntity() instanceof Mob mob)) {
            return;
        }

        if (!mob.hasEffect(PBMEffects.PROVOKE_DEBUFF.get())) {
            return;
        }

        if (mob.tickCount % 10 != 0) {
            return;
        }

        if (!mob.getPersistentData().contains(PROVOKER_ENTITY_ID_KEY)) {
            LivingEntity applier = mob.getLastHurtByMob();
            if (applier != null) {
                mob.getPersistentData().putInt(PROVOKER_ENTITY_ID_KEY, applier.getId());
                mob.setTarget(applier);
            }
            return;
        }

        int applierId = mob.getPersistentData().getInt(PROVOKER_ENTITY_ID_KEY);
        if (mob.level().getEntity(applierId) instanceof LivingEntity applier && applier.isAlive() && !(applier instanceof ServerPlayer player && player.isSpectator())) {
            if (mob.getTarget() != applier) {
                mob.setTarget(applier);
            }
        } else {
            mob.getPersistentData().remove(PROVOKER_ENTITY_ID_KEY);
        }
    }
}