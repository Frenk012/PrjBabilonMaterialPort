package com.rave.projectbabylonmaterials.handler;

import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ProjectBabylonMaterials.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class LivingEntityHealthHandler {
    private static final UUID UNIVERSAL_HEALTH_MODIFIER_ID = UUID.fromString("587d9b2d-df04-4a34-b1e7-37b923648ca3");
    private static final String UNIVERSAL_HEALTH_MODIFIER_NAME = "project_babylon_materials.universal_living_health";
    private static final double UNIVERSAL_HEALTH_BONUS = 10.0D;

    private LivingEntityHealthHandler() {
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof LivingEntity livingEntity) || livingEntity instanceof ServerPlayer) {
            return;
        }

        applyUniversalHealthBonus(livingEntity);
    }

    public static void applyUniversalHealthBonus(LivingEntity entity) {
        AttributeInstance maxHealth = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }

        double previousMaxHealth = entity.getMaxHealth();
        float previousHealth = entity.getHealth();
        AttributeModifier existing = maxHealth.getModifier(UNIVERSAL_HEALTH_MODIFIER_ID);
        if (existing != null) {
            maxHealth.removeModifier(UNIVERSAL_HEALTH_MODIFIER_ID);
        }

        maxHealth.addTransientModifier(new AttributeModifier(
                UNIVERSAL_HEALTH_MODIFIER_ID,
                UNIVERSAL_HEALTH_MODIFIER_NAME,
                UNIVERSAL_HEALTH_BONUS,
                AttributeModifier.Operation.ADDITION
        ));

        double newMaxHealth = entity.getMaxHealth();
        if (Math.abs(newMaxHealth - previousMaxHealth) <= 0.0001D) {
            return;
        }

        float adjustedHealth = previousHealth;
        if (newMaxHealth > previousMaxHealth) {
            adjustedHealth = (float) Math.min(newMaxHealth, previousHealth + (newMaxHealth - previousMaxHealth));
        } else if (newMaxHealth < previousMaxHealth) {
            adjustedHealth = (float) Math.min(previousHealth, newMaxHealth);
        }

        entity.setHealth(adjustedHealth);
    }
}
