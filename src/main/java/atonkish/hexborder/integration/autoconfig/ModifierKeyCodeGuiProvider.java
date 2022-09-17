package atonkish.hexborder.integration.autoconfig;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.gui.registry.api.GuiProvider;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import me.shedaniel.clothconfig2.gui.entries.KeyCodeEntry;

import net.minecraft.text.Text;

public class ModifierKeyCodeGuiProvider implements GuiProvider {
    @Override
    public List<AbstractConfigListEntry> get(String i18n, Field field, Object config, Object defaults,
            GuiRegistryAccess registry) {
        if (field.isAnnotationPresent(ConfigEntry.Gui.Excluded.class)) {
            return Collections.emptyList();
        }

        KeyCodeEntry entry = ConfigEntryBuilder.create()
                .startModifierKeyCodeField(Text.translatable(i18n),
                        Utils.getUnsafely(field, config, ModifierKeyCode.unknown()))
                .setModifierDefaultValue(() -> Utils.getUnsafely(field, config))
                .setModifierSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue))
                .build();

        return Collections.singletonList(entry);
    }
}
