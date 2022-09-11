package atonkish.hexborder.integration.autoconfig;

import me.shedaniel.autoconfig.ConfigData;

public interface ModConfigData extends ConfigData {
    public default void validateOnSave() {
    }
}
