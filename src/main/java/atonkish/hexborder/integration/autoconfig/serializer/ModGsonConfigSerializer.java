package atonkish.hexborder.integration.autoconfig.serializer;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;

import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Key;

public class ModGsonConfigSerializer<T extends ConfigData> extends GsonConfigSerializer<T> {
    public ModGsonConfigSerializer(Config definition, Class<T> configClass, Gson gson) {
        super(definition, configClass, gson);
    }

    public ModGsonConfigSerializer(Config definition, Class<T> configClass) {
        this(definition, configClass,
                new GsonBuilder()
                        .registerTypeAdapter(ModifierKeyCode.class, new ModifierKeyCodeSerializer())
                        .registerTypeAdapter(ModifierKeyCode.class, new ModifierKeyCodeDeserializer())
                        .setPrettyPrinting().create());
    }

    private static class ModifierKeyCodeSerializer implements JsonSerializer<ModifierKeyCode> {
        @Override
        public JsonElement serialize(ModifierKeyCode keyCode, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("keyCode", keyCode.getKeyCode().getTranslationKey());
            object.addProperty("modifier", keyCode.getModifier().getValue());

            return object;
        }
    }

    private static class ModifierKeyCodeDeserializer implements JsonDeserializer<ModifierKeyCode> {
        @Override
        public ModifierKeyCode deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            JsonObject object = json.getAsJsonObject();

            String serializedKeyCode = object.get("keyCode").getAsString();
            Key keyCode = InputUtil.fromTranslationKey(serializedKeyCode);
            if (keyCode.equals(InputUtil.UNKNOWN_KEY)) {
                return ModifierKeyCode.unknown();
            }

            short serializedModifier = object.get("modifier").getAsShort();
            Modifier modifier = Modifier.of(serializedModifier);

            return ModifierKeyCode.of(keyCode, modifier);
        }
    }
}
