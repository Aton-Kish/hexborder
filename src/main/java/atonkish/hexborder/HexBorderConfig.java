package atonkish.hexborder;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;

import net.minecraft.client.util.InputUtil.Type;

import org.lwjgl.glfw.GLFW;

import atonkish.hexborder.integration.autoconfig.ModConfigData;
import atonkish.hexborder.integration.autoconfig.annotation.ModConfigEntry;

@Config(name = HexBorderMod.MOD_ID)
public class HexBorderConfig implements ModConfigData {
    @ModConfigEntry.HexagonSide(min = 4)
    public int side = 8;

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Offset offset = new Offset();

    @ConfigEntry.Gui.CollapsibleObject()
    public HexBorderColors mainColors = new HexBorderColors(0x800000FF, 0x80FF0000, 0x80009B9B, 0x80FFFF00);

    @ConfigEntry.Gui.CollapsibleObject()
    public HexBorderColors neighborColors = new HexBorderColors(0x00000000, 0x80FF0000, 0x80009B9B, 0x80FFFF00);

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public F3KeyBindings f3KeyBindings = new F3KeyBindings();

    public static class Offset {
        public int x = 0;
        public int z = 0;
    }

    public static class HexBorderColors {
        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int originVerticalLine;

        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int vertexVerticalLine;

        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int sideHorizontalMainLine;

        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int sideHorizontalSubLine;

        public HexBorderColors(int originVerticalLine, int vertexVerticalLine,
                int sideHorizontalMainLine, int sideHorizontalSubLine) {
            this.originVerticalLine = originVerticalLine;
            this.vertexVerticalLine = vertexVerticalLine;
            this.sideHorizontalMainLine = sideHorizontalMainLine;
            this.sideHorizontalSubLine = sideHorizontalSubLine;
        }
    }

    public static class F3KeyBindings {
        public ModifierKeyCode toggleShowBorderKey = ModifierKeyCode.of(
                Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_X),
                Modifier.none());
    }
}
