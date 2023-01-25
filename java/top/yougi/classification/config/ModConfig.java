package top.yougi.classification.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;

public class ModConfig {
    private static ForgeConfigSpec config;
    public static ForgeConfigSpec.ConfigValue<? extends Integer> searchRange;

    public static void load() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("search range").push("range");

        searchRange = builder.defineInRange("searchRange", 10, 1, 50);

        builder.pop();
        config = builder.build();
    }

    public static void register() {
        load();
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, getConfig());
    }

    public static ForgeConfigSpec getConfig() {
        return config;
    }

}
