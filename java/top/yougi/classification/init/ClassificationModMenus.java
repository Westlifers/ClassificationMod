
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package top.yougi.classification.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.yougi.classification.Classification;
import top.yougi.classification.client.gui.ClassManagerMenu;

public class ClassificationModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Classification.MODID);
	public static final RegistryObject<MenuType<ClassManagerMenu>> CLASS_MANAGER = REGISTRY.register("class_manager",
			() -> IForgeMenuType.create(ClassManagerMenu::new));
}
