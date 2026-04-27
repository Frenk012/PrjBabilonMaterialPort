package com.rave.projectbabylonmaterials.handler;

import com.rave.projectbabylonmaterials.init.PBAttributes;
import com.rave.projectbabylonmaterials.network.PBNetwork;
import com.rave.projectbabylonmaterials.network.client.ClientboundCritEffectPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

public final class CritDamageHandler {

    private CritDamageHandler() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0.0F || event.getEntity().level().isClientSide) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        double critChance = attacker.getAttributeValue(PBAttributes.CRIT_CHANCE.get());
        if (critChance <= 0.0D) {
            return;
        }

        if (attacker.getRandom().nextDouble() >= critChance) {
            return;
        }

        double critDamage = attacker.getAttributeValue(PBAttributes.CRIT_DAMAGE.get());
        if (critDamage <= 0.0D) {
            return;
        }

        event.setAmount((float) (event.getAmount() * (1.0D + critDamage)));
        spawnCritFeedback(event.getEntity(), attacker);
    }

    private static void spawnCritFeedback(LivingEntity target, LivingEntity attacker) {
        if (!(target.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        double x = target.getX();
        double y = target.getY() + target.getBbHeight() * 0.65D;
        double z = target.getZ();

        serverLevel.sendParticles(ParticleTypes.CRIT, x, y, z, 14, 0.3D, 0.4D, 0.3D, 0.15D);

        double popupX = x + (target.getRandom().nextDouble() - 0.5D) * 0.6D;
        double popupY = target.getY() + target.getBbHeight() + 0.45D;
        double popupZ = z + (target.getRandom().nextDouble() - 0.5D) * 0.6D;

        ClientboundCritEffectPacket packet = new ClientboundCritEffectPacket(popupX, popupY, popupZ);
        PBNetwork.CHANNEL.send(PacketDistributor.NEAR.with(
                PacketDistributor.TargetPoint.p(popupX, popupY, popupZ, 32.0D, serverLevel.dimension())
        ), packet);
    }
}

