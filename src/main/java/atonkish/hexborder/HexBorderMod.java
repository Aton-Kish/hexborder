package atonkish.hexborder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;

import net.fabricmc.api.ModInitializer;

import atonkish.hexborder.integration.autoconfig.gui.ModGuiProviders;
import atonkish.hexborder.integration.autoconfig.serializer.ModGsonConfigSerializer;

public class HexBorderMod implements ModInitializer {
	public static final String MOD_ID = "hexborder";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ConfigHolder<HexBorderConfig> CONFIG_MANAGER;

	@Override
	public void onInitialize() {
		// Auto Config
		ModGuiProviders.apply(AutoConfig.getGuiRegistry(HexBorderConfig.class));
		CONFIG_MANAGER = AutoConfig.register(HexBorderConfig.class, ModGsonConfigSerializer::new);
	}
}
