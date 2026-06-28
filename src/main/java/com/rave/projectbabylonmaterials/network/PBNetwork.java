package com.rave.projectbabylonmaterials.network;

import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import com.rave.projectbabylonmaterials.network.client.ClientboundCritEffectPacket;
import com.rave.projectbabylonmaterials.network.client.ClientboundDragonsteelCooldownPacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class PBNetwork {

    private static final String PROTOCOL_VERSION = "1";

    private PBNetwork() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(ProjectBabylonMaterials.MODID).versioned(PROTOCOL_VERSION);

        registrar.playToClient(
                ClientboundCritEffectPacket.TYPE,
                ClientboundCritEffectPacket.STREAM_CODEC,
                ClientboundCritEffectPacket::handle
        );
        registrar.playToClient(
                ClientboundDragonsteelCooldownPacket.TYPE,
                ClientboundDragonsteelCooldownPacket.STREAM_CODEC,
                ClientboundDragonsteelCooldownPacket::handle
        );
    }
}
