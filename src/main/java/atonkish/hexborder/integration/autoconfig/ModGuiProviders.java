package atonkish.hexborder.integration.autoconfig;

import java.util.Collections;
import java.util.Optional;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;

import net.minecraft.text.Text;

import atonkish.hexborder.HexBorderMod;
import atonkish.hexborder.integration.autoconfig.annotation.ModConfigEntry;

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

        registry.registerAnnotationProvider(
                (i18n, field, config, defaults, guiProvider) -> {
                    ModConfigEntry.HexagonSide side = field.getAnnotation(ModConfigEntry.HexagonSide.class);

                    return Collections.singletonList(
                            ENTRY_BUILDER
                                    .startIntField(Text.translatable(i18n),
                                            Utils.getUnsafely(field, config, (int) side.min()))
                                    .setDefaultValue(() -> Utils.getUnsafely(field, defaults))
                                    .setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue))
                                    .setMin((int) side.min())
                                    .setErrorSupplier((value) -> {
                                        if (value % 2 != 0) {
                                            return Optional
                                                    .of(Text.translatable("text.cloth-config."
                                                            + HexBorderMod.MOD_ID
                                                            + ".error.odd"));
                                        }

                                        return Optional.empty();
                                    })
                                    .build());
                },
                field -> field.getType() == int.class || field.getType() == Integer.class,
                ModConfigEntry.HexagonSide.class);

        registry.registerAnnotationProvider(
                (i18n, field, config, defaults, guiProvider) -> {
                    ModConfigEntry.HexagonSide side = field.getAnnotation(ModConfigEntry.HexagonSide.class);

                    return Collections.singletonList(
                            ENTRY_BUILDER
                                    .startLongField(Text.translatable(i18n),
                                            Utils.getUnsafely(field, config, side.min()))
                                    .setDefaultValue(() -> Utils.getUnsafely(field, defaults))
                                    .setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue))
                                    .setMin(side.min())
                                    .setErrorSupplier((value) -> {
                                        if (value % 2 != 0) {
                                            return Optional
                                                    .of(Text.translatable("text.cloth-config."
                                                            + HexBorderMod.MOD_ID
                                                            + ".error.odd"));
                                        }

                                        return Optional.empty();
                                    })
                                    .build());
                },
                field -> field.getType() == long.class || field.getType() == Long.class,
                ModConfigEntry.HexagonSide.class);

        return registry;
    }

    private static GuiRegistry applyPredicateProvider(GuiRegistry registry) {
        registry.registerPredicateProvider(
                (i18n, field, config, defaults, guiProvider) -> {
                    return Collections.singletonList(
                            ENTRY_BUILDER
                                    .startModifierKeyCodeField(Text.translatable(i18n),
                                            Utils.getUnsafely(field, config, ModifierKeyCode.unknown()))
                                    .setModifierDefaultValue(() -> Utils.getUnsafely(field, defaults))
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
