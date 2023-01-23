
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package top.yougi.classification.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.yougi.classification.client.gui.AddClassScreen;
import top.yougi.classification.client.gui.ClassManagerScreen;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClassificationModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(ClassificationModMenus.CLASS_MANAGER.get(), ClassManagerScreen::new);
			MenuScreens.register(ClassificationModMenus.ADD_CLASS.get(), AddClassScreen::new);
		});
	}
}
