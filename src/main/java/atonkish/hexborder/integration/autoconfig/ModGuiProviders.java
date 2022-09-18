package atonkish.hexborder.integration.autoconfig;

import java.util.Collections;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import net.minecraft.text.Text;

public class ModGuiProviders {
    private static final ConfigEntryBuilder ENTRY_BUILDER = ConfigEntryBuilder.create();

    public static GuiRegistry apply(GuiRegistry registry) {
        ModGuiProviders.applyAnnotationProvider(registry);

        ModGuiProviders.applyPredicateProvider(registry);

        ModGuiProviders.applyTypeProvider(registry);

        return registry;
    }

    private static GuiRegistry applyAnnotationProvider(GuiRegistry registry) {
        registry.registerAnnotationProvider(
                (i18n, field, config, defaults, guiProvider) -> Collections.emptyList(),
                ConfigEntry.Gui.Excluded.class);

        return registry;
    }

    private static GuiRegistry applyPredicateProvider(GuiRegistry registry) {
        registry.registerPredicateProvider(
                (i18n, field, config, defaults, guiProvider) -> {
                    return Collections.singletonList(
                            ENTRY_BUILDER
                                    .startModifierKeyCodeField(Text.translatable(i18n),
                                            Utils.getUnsafely(field, config, ModifierKeyCode.unknown()))
                                    .setModifierDefaultValue(() -> Utils.getUnsafely(field, config))
                                    .setModifierSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue))
                                    .build());
                },
                field -> field.getType() == ModifierKeyCode.class);

        return registry;
    }

    private static GuiRegistry applyTypeProvider(GuiRegistry registry) {
        return registry;
    }
}
