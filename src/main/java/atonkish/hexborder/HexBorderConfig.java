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

    @ConfigEntry.BoundedDiscrete(min = 4, max = 32)
    public int viewDistance = 16;

    @ConfigEntry.Category("colors")
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public HexBorderColors mainColors = new HexBorderColors(0xFF0000FF, 0xFFFF0000, 0xFF009B9B, 0xFFFFFF00, 0xFF0000FF);

    @ConfigEntry.Category("colors")
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public HexBorderColors neighborColors = new HexBorderColors(0x00000000, 0xFFFF0000, 0xFF009B9B, 0xFFFFFF00, 0x00000000);

    @ConfigEntry.Category("f3KeyBindings")
    @ConfigEntry.Gui.TransitiveObject
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

        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int grid;

        public HexBorderColors(int originVerticalLine, int vertexVerticalLine,
                int sideHorizontalMainLine, int sideHorizontalSubLine, int grid) {
            this.originVerticalLine = originVerticalLine;
            this.vertexVerticalLine = vertexVerticalLine;
            this.sideHorizontalMainLine = sideHorizontalMainLine;
            this.sideHorizontalSubLine = sideHorizontalSubLine;
            this.grid = grid;
        }
    }

    public static class F3KeyBindings {
        public ModifierKeyCode toggleShowBorderKey = ModifierKeyCode.of(
                Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_X),
                Modifier.none());
        public ModifierKeyCode incOffsetXKey = ModifierKeyCode.of(
                Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_RIGHT),
                Modifier.none());
        public ModifierKeyCode decOffsetXKey = ModifierKeyCode.of(
                Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_LEFT),
                Modifier.none());
        public ModifierKeyCode incOffsetZKey = ModifierKeyCode.of(
                Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_UP),
                Modifier.none());
        public ModifierKeyCode decOffsetZKey = ModifierKeyCode.of(
                Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_DOWN),
                Modifier.none());
    }
}
