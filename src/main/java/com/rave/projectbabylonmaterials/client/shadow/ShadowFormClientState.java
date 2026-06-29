package com.rave.projectbabylonmaterials.client.shadow;

import com.rave.projectbabylonmaterials.client.photon.PBMPhotonEffectHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ShadowFormClientState {
    private static final Set<Integer> CONCEALED_ENTITIES = ConcurrentHashMap.newKeySet();
    private static final Set<Integer> APPLIED_VISUALS = ConcurrentHashMap.newKeySet();

    private ShadowFormClientState() {
    }

    public static void setConcealed(int entityId, boolean concealed) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        boolean wasConcealed = CONCEALED_ENTITIES.contains(entityId);

        if (concealed) {
            CONCEALED_ENTITIES.add(entityId);
            if (!wasConcealed && level != null) {
                Entity entity = level.getEntity(entityId);
                PBMPhotonEffectHelper.spawnShadowFormTransition(entity, true);
            }
            if (level != null) {
                apply(level.getEntity(entityId));
            }
            return;
        }

        CONCEALED_ENTITIES.remove(entityId);
        if (wasConcealed && level != null) {
            Entity entity = level.getEntity(entityId);
            PBMPhotonEffectHelper.spawnShadowFormTransition(entity, false);
            restore(entity);
        } else if (level != null) {
            restore(level.getEntity(entityId));
        } else {
            APPLIED_VISUALS.remove(entityId);
        }
    }

    public static boolean isConcealed(Entity entity) {
        return entity != null && CONCEALED_ENTITIES.contains(entity.getId());
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) {
            clear();
            return;
        }

        for (Integer entityId : Set.copyOf(CONCEALED_ENTITIES)) {
            Entity entity = level.getEntity(entityId);
            if (entity == null || !entity.isAlive()) {
                CONCEALED_ENTITIES.remove(entityId);
                APPLIED_VISUALS.remove(entityId);
                continue;
            }

            apply(entity);
        }

        for (Integer entityId : Set.copyOf(APPLIED_VISUALS)) {
            if (CONCEALED_ENTITIES.contains(entityId)) {
                continue;
            }

            restore(level.getEntity(entityId));
        }
    }

    @SubscribeEvent
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
        if (isConcealed(event.getEntity())) {
            apply(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && isConcealed(minecraft.player)) {
            event.setCanceled(true);
        }
    }

    public static void clear() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level != null) {
            for (Integer entityId : Set.copyOf(APPLIED_VISUALS)) {
                restore(level.getEntity(entityId));
            }
        }

        CONCEALED_ENTITIES.clear();
        APPLIED_VISUALS.clear();
    }

    private static void apply(Entity entity) {
        if (entity == null) {
            return;
        }

        APPLIED_VISUALS.add(entity.getId());
        entity.setInvisible(true);
        entity.setGlowingTag(true);
    }

    private static void restore(Entity entity) {
        if (entity == null) {
            return;
        }

        APPLIED_VISUALS.remove(entity.getId());
        if (!(entity instanceof LivingEntity living) || !living.hasEffect(MobEffects.INVISIBILITY)) {
            entity.setInvisible(false);
        }
        if (!(entity instanceof LivingEntity living) || !living.hasEffect(MobEffects.GLOWING)) {
            entity.setGlowingTag(false);
        }
    }
}