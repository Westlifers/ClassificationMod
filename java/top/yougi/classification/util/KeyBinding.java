package top.yougi.classification.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_CLASSIFICATION = "key.category.classification.classification";
    public static final String KEY_OPEN_CLASS_GUI = "key.classification.open_class_gui";

    public static final KeyMapping CLASS_GUI_KEY = new KeyMapping(KEY_OPEN_CLASS_GUI, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_CLASSIFICATION);

}
