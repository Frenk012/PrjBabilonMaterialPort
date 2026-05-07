package com.rave.projectbabylonmaterials.client.overlay;

import com.rave.projectbabylonmaterials.ProjectBabylonMaterials;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProjectBabylonMaterials.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class DragonsteelCooldownClientState {
    private static int remainingTicks;

    private DragonsteelCooldownClientState() {
    }

    public static int getRemainingTicks() {
        return remainingTicks;
    }

    public static void setRemainingTicks(int remainingTicks) {
        DragonsteelCooldownClientState.remainingTicks = Math.max(0, remainingTicks);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && remainingTicks > 0) {
            remainingTicks--;
        }
    }
}