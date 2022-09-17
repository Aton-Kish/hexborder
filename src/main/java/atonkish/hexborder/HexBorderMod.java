package atonkish.hexborder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.GsonBuilder;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;

import net.fabricmc.api.ModInitializer;

import atonkish.hexborder.integration.autoconfig.ModSave;
import atonkish.hexborder.integration.autoconfig.ModifierKeyCodeGsonAdapter;
import atonkish.hexborder.integration.autoconfig.ModifierKeyCodeGuiProvider;

public class HexBorderMod implements ModInitializer {
	public static final String MOD_ID = "hexborder";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ConfigHolder<HexBorderConfig> CONFIG_MANAGER;

	@Override
	public void onInitialize() {
		// Auto Config
		GuiRegistry registry = AutoConfig.getGuiRegistry(HexBorderConfig.class);
		registry.registerPredicateProvider(new ModifierKeyCodeGuiProvider(),
				field -> field.getType() == ModifierKeyCode.class);

		CONFIG_MANAGER = AutoConfig.register(HexBorderConfig.class,
				(definition, configClass) -> new GsonConfigSerializer<>(definition, configClass, new GsonBuilder()
						.registerTypeAdapter(ModifierKeyCode.class, new ModifierKeyCodeGsonAdapter.Serializer())
						.registerTypeAdapter(ModifierKeyCode.class, new ModifierKeyCodeGsonAdapter.Deserializer())
						.create()));
		CONFIG_MANAGER.registerSaveListener(new ModSave<HexBorderConfig>());
	}
}
