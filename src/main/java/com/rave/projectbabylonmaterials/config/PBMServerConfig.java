package com.rave.projectbabylonmaterials.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class PBMServerConfig {
    public static final ForgeConfigSpec SPEC;
    private static final Holder HOLDER;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        HOLDER = new Holder(builder);
        SPEC = builder.build();
    }

    private PBMServerConfig() {
    }

    public static double getPlayerBaseHealth() {
        return HOLDER.playerBaseHealth.get();
    }

    private static final class Holder {
        private final ForgeConfigSpec.DoubleValue playerBaseHealth;

        private Holder(ForgeConfigSpec.Builder builder) {
            builder.push("combat");
            playerBaseHealth = builder
                    .comment("Base max health for players. Vanilla default is 20.0.")
                    .defineInRange("playerBaseHealth", 30.0D, 1.0D, 1024.0D);
            builder.pop();
        }
    }
}