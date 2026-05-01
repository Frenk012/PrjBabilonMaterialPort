package com.rave.projectbabylonmaterials.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class PBMClientConfig {
    public static final ForgeConfigSpec SPEC;
    private static final Holder HOLDER;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        HOLDER = new Holder(builder);
        SPEC = builder.build();
    }

    private PBMClientConfig() {
    }

    public static boolean showCustomCombatHud() {
        return HOLDER.showCustomCombatHud.get();
    }

    public static boolean hideVanillaArmorHud() {
        return HOLDER.hideVanillaArmorHud.get();
    }

    private static final class Holder {
        private final ForgeConfigSpec.BooleanValue showCustomCombatHud;
        private final ForgeConfigSpec.BooleanValue hideVanillaArmorHud;

        private Holder(ForgeConfigSpec.Builder builder) {
            builder.push("hud");
            showCustomCombatHud = builder
                    .comment("Show the custom armor and toughness HUD next to the hotbar.")
                    .define("showCustomCombatHud", true);
            hideVanillaArmorHud = builder
                    .comment("Hide the vanilla armor bar when the custom combat HUD is enabled.")
                    .define("hideVanillaArmorHud", true);
            builder.pop();
        }
    }
}