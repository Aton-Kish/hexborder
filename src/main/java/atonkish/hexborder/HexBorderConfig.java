package atonkish.hexborder;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.client.util.InputUtil.Type;

import org.lwjgl.glfw.GLFW;

import atonkish.hexborder.integration.autoconfig.annotation.ModConfigEntry;

@Config(name = HexBorderMod.MOD_ID)
public class HexBorderConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public static final int HEXAGON_SIDE_DEFAULT = 8;
    @ConfigEntry.Gui.Excluded
    public static final int HEXAGON_SIDE_MIN = 2;

    @ConfigEntry.Gui.Excluded
    public static final int GRID_DISTANCE_DEFAULT = 8;
    @ConfigEntry.Gui.Excluded
    public static final int GRID_DISTANCE_MIN = 4;
    @ConfigEntry.Gui.Excluded
    public static final int GRID_DISTANCE_MAX = 32;

    @ModConfigEntry.HexagonSide(min = HEXAGON_SIDE_MIN)
    public int side = HEXAGON_SIDE_DEFAULT;

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
    public Offset offset = new Offset();

    @ConfigEntry.BoundedDiscrete(min = GRID_DISTANCE_MIN, max = GRID_DISTANCE_MAX)
    public int gridDistance = GRID_DISTANCE_DEFAULT;

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
        // toggle
        public Key toggleShowBorderKey = Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_X);

        // side
        public Key incSideKey = Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_PAGE_UP);
        public Key decSideKey = Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_PAGE_DOWN);

        // offset
        public Key incOffsetXKey = Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_RIGHT);
        public Key decOffsetXKey = Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_LEFT);
        public Key incOffsetZKey = Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_UP);
        public Key decOffsetZKey = Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_DOWN);
    }
}
