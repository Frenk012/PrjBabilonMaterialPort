package com.rave.projectbabylonmaterials.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.particle.EpicFightParticles;

import java.util.function.Supplier;

public record ClientboundShadowFormAfterimagePacket(int entityId) {

    public static void encode(ClientboundShadowFormAfterimagePacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.entityId);
    }

    public static ClientboundShadowFormAfterimagePacket decode(FriendlyByteBuf buffer) {
        return new ClientboundShadowFormAfterimagePacket(buffer.readVarInt());
    }

    public static void handle(ClientboundShadowFormAfterimagePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level == null) {
                return;
            }

            Entity entity = minecraft.level.getEntity(packet.entityId());
            if (entity == null || !entity.isAlive()) {
                return;
            }

            minecraft.level.addParticle(EpicFightParticles.WHITE_AFTERIMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0.0D, 0.0D);
        });
        context.setPacketHandled(true);
    }
}