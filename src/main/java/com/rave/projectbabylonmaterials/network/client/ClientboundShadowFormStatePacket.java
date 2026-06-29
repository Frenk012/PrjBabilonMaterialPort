package com.rave.projectbabylonmaterials.network.client;

import com.rave.projectbabylonmaterials.client.shadow.ShadowFormClientState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClientboundShadowFormStatePacket(int entityId, boolean concealed) {

    public static void encode(ClientboundShadowFormStatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.entityId);
        buffer.writeBoolean(packet.concealed);
    }

    public static ClientboundShadowFormStatePacket decode(FriendlyByteBuf buffer) {
        return new ClientboundShadowFormStatePacket(buffer.readVarInt(), buffer.readBoolean());
    }

    public static void handle(ClientboundShadowFormStatePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ShadowFormClientState.setConcealed(packet.entityId(), packet.concealed()));
        context.setPacketHandled(true);
    }
}