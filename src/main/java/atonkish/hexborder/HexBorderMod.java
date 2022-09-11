package atonkish.hexborder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import net.fabricmc.api.ModInitializer;

public class HexBorderMod implements ModInitializer {
	public static final String MOD_ID = "reputation";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static HexBorderConfig CONFIG;

	@Override
	public void onInitialize() {
		// Auto Config
		AutoConfig.register(HexBorderConfig.class, GsonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(HexBorderConfig.class).getConfig();
	}
}
