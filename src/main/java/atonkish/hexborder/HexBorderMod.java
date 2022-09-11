package atonkish.hexborder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import net.fabricmc.api.ModInitializer;

import atonkish.hexborder.integration.autoconfig.ModSave;
public class HexBorderMod implements ModInitializer {
	public static final String MOD_ID = "hexborder";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static HexBorderConfig CONFIG;

	@Override
	public void onInitialize() {
		// Auto Config
		ConfigHolder<HexBorderConfig> manager = AutoConfig.register(HexBorderConfig.class, GsonConfigSerializer::new);
		manager.registerSaveListener(new ModSave<HexBorderConfig>());
		CONFIG = AutoConfig.getConfigHolder(HexBorderConfig.class).getConfig();
	}
}
