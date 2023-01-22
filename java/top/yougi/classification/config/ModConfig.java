package top.yougi.classification.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModConfig {
    private static ForgeConfigSpec config;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ClassNames;
    public static ForgeConfigSpec.ConfigValue<List<? extends ArrayList<String>>> ClassItems;
    public static ForgeConfigSpec.ConfigValue<List<? extends ArrayList<Integer>>> ClassChests;

    public static void load() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Classes").push("Classes");

        ArrayList<String> DEFAULT_CLASSNAMES = new ArrayList<>();
        DEFAULT_CLASSNAMES.add("DefaultClass");
        ClassNames = builder.defineList("ClassNames", DEFAULT_CLASSNAMES, a -> true);

        ArrayList<ArrayList<String>> DEFAULT_CLASSITEMS = new ArrayList<>();
        DEFAULT_CLASSITEMS.add(new ArrayList<>(Arrays.asList("item.minecraft.bread", "item.minecraft.button")));
        ClassItems = builder.defineList("ClassItems", DEFAULT_CLASSITEMS, a -> true);

        ArrayList<ArrayList<Integer>> DEFAULT_CLASSCHESTS = new ArrayList<>();
        DEFAULT_CLASSCHESTS.add(new ArrayList<>(Arrays.asList(0, 0, 0)));
        ClassChests = builder.defineList("ClassChests", DEFAULT_CLASSCHESTS, a -> true);

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
