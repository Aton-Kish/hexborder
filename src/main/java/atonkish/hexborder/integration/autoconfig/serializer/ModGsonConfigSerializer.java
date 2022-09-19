package atonkish.hexborder.integration.autoconfig.serializer;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Key;

public class ModGsonConfigSerializer<T extends ConfigData> extends GsonConfigSerializer<T> {
    public ModGsonConfigSerializer(Config definition, Class<T> configClass, Gson gson) {
        super(definition, configClass, gson);
    }

    public ModGsonConfigSerializer(Config definition, Class<T> configClass) {
        this(definition, configClass,
                new GsonBuilder()
                        .registerTypeAdapter(Key.class, new KeySerializer())
                        .registerTypeAdapter(Key.class, new KeyDeserializer())
                        .setPrettyPrinting().create());
    }

    private static class KeySerializer implements JsonSerializer<Key> {
        @Override
        public JsonElement serialize(Key keyCode, Type type, JsonSerializationContext context) {
            JsonPrimitive key = new JsonPrimitive(keyCode.getTranslationKey());
            return key;
        }
    }

    private static class KeyDeserializer implements JsonDeserializer<Key> {
        @Override
        public Key deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            String key = json.getAsString();

            Key keyCode = InputUtil.fromTranslationKey(key);
            return keyCode;
        }
    }
}
