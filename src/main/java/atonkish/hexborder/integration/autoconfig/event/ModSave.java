package atonkish.hexborder.integration.autoconfig.event;

import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.event.ConfigSerializeEvent.Save;

import net.minecraft.util.ActionResult;

import atonkish.hexborder.integration.autoconfig.ModConfigData;

public class ModSave<T extends ModConfigData> implements Save<T> {
    @Override
    public ActionResult onSave(ConfigHolder<T> manager, T config) {
        config.validateOnSave();
        return ActionResult.PASS;
    }
}
