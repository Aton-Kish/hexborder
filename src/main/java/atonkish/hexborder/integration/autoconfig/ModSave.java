package atonkish.hexborder.integration.autoconfig;

import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.event.ConfigSerializeEvent.Save;

import net.minecraft.util.ActionResult;

public class ModSave<T extends ModConfigData> implements Save<T> {
    @Override
    public ActionResult onSave(ConfigHolder<T> manager, T config) {
        config.validateOnSave();
        return ActionResult.PASS;
    }
}
