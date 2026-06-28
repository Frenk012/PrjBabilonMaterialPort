package com.rave.projectbabylonmaterials.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class PBMClientConfig {
    public static final ModConfigSpec SPEC;
    private static final Holder HOLDER;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
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
        private final ModConfigSpec.BooleanValue showCustomCombatHud;
        private final ModConfigSpec.BooleanValue hideVanillaArmorHud;

        private Holder(ModConfigSpec.Builder builder) {
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
