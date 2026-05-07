package com.rave.projectbabylonmaterials.network.client;

import com.rave.projectbabylonmaterials.client.overlay.DragonsteelCooldownClientState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClientboundDragonsteelCooldownPacket(int remainingTicks) {

    public static void encode(ClientboundDragonsteelCooldownPacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.remainingTicks);
    }

    public static ClientboundDragonsteelCooldownPacket decode(FriendlyByteBuf buffer) {
        return new ClientboundDragonsteelCooldownPacket(buffer.readVarInt());
    }

    public static void handle(ClientboundDragonsteelCooldownPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DragonsteelCooldownClientState.setRemainingTicks(packet.remainingTicks));
        context.setPacketHandled(true);
    }
}