package atonkish.hexborder.integration.autoconfig;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;

import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Key;

public class ModifierKeyCodeGsonAdapter {
    public static class Serializer implements JsonSerializer<ModifierKeyCode> {
        @Override
        public JsonElement serialize(ModifierKeyCode keyCode, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("keyCode", keyCode.getKeyCode().getTranslationKey());
            object.addProperty("modifier", keyCode.getModifier().getValue());

            return object;
        }
    }

    public static class Deserializer implements JsonDeserializer<ModifierKeyCode> {
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
