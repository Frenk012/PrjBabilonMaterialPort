package com.rave.projectbabylonmaterials.network.client;

import com.rave.projectbabylonmaterials.client.effect.CritEffectRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClientboundCritEffectPacket(double x, double y, double z) {

    public static void encode(ClientboundCritEffectPacket packet, FriendlyByteBuf buffer) {
        buffer.writeDouble(packet.x);
        buffer.writeDouble(packet.y);
        buffer.writeDouble(packet.z);
    }

    public static ClientboundCritEffectPacket decode(FriendlyByteBuf buffer) {
        return new ClientboundCritEffectPacket(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void handle(ClientboundCritEffectPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> CritEffectRenderer.spawn(packet.x, packet.y, packet.z));
        context.setPacketHandled(true);
    }
}

