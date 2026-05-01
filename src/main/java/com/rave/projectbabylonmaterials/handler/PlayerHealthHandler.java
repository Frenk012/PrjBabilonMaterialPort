package com.rave.projectbabylonmaterials.handler;

import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import com.rave.projectbabylonmaterials.config.PBMServerConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ProjectBabylonMaterials.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlayerHealthHandler {
    private static final UUID PLAYER_BASE_HEALTH_MODIFIER_ID = UUID.fromString("3f58d9b2-e1d7-43f1-81b4-cb690f0a8d8d");
    private static final String PLAYER_BASE_HEALTH_MODIFIER_NAME = "project_babylon_materials.player_base_health";
    private static final double VANILLA_PLAYER_BASE_HEALTH = 20.0D;

    private PlayerHealthHandler() {
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            applyConfiguredMaxHealth(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            applyConfiguredMaxHealth(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            applyConfiguredMaxHealth(player);
        }
    }

    @SubscribeEvent
    public static void onConfigLoading(ModConfigEvent.Loading event) {
        if (isServerConfig(event.getConfig())) {
            refreshOnlinePlayers();
        }
    }

    @SubscribeEvent
    public static void onConfigReloading(ModConfigEvent.Reloading event) {
        if (isServerConfig(event.getConfig())) {
            refreshOnlinePlayers();
        }
    }

    private static boolean isServerConfig(ModConfig config) {
        return config.getModId().equals(ProjectBabylonMaterials.MODID) && config.getType() == ModConfig.Type.SERVER;
    }

    private static void refreshOnlinePlayers() {
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            applyConfiguredMaxHealth(player);
        }
    }

    private static void applyConfiguredMaxHealth(ServerPlayer player) {
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }

        double previousMaxHealth = player.getMaxHealth();
        float previousHealth = player.getHealth();
        AttributeModifier existing = maxHealth.getModifier(PLAYER_BASE_HEALTH_MODIFIER_ID);
        if (existing != null) {
            maxHealth.removeModifier(PLAYER_BASE_HEALTH_MODIFIER_ID);
        }

        double bonusHealth = PBMServerConfig.getPlayerBaseHealth() - VANILLA_PLAYER_BASE_HEALTH;
        if (Math.abs(bonusHealth) > 0.0001D) {
            maxHealth.addTransientModifier(new AttributeModifier(
                    PLAYER_BASE_HEALTH_MODIFIER_ID,
                    PLAYER_BASE_HEALTH_MODIFIER_NAME,
                    bonusHealth,
                    AttributeModifier.Operation.ADDITION
            ));
        }

        double newMaxHealth = player.getMaxHealth();
        if (previousMaxHealth > 0.0D && Math.abs(newMaxHealth - previousMaxHealth) > 0.0001D) {
            float adjustedHealth = (float) Mth.clamp((previousHealth / previousMaxHealth) * newMaxHealth, 0.0D, newMaxHealth);
            player.setHealth(adjustedHealth);
        }
    }
}