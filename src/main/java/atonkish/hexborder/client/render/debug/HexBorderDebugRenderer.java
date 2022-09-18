package atonkish.hexborder.client.render.debug;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.debug.DebugRenderer.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;

import atonkish.hexborder.HexBorderConfig;
import atonkish.hexborder.HexBorderMod;
import atonkish.hexborder.HexBorderConfig.HexBorderColors;
import atonkish.hexborder.util.math.Vec2;
import atonkish.hexborder.util.polygon.Hexagon;
import atonkish.hexborder.util.polygon.Polygon;

@Environment(value = EnvType.CLIENT)
public class HexBorderDebugRenderer implements Renderer {
    private static final int TRANSPARENT = ColorHelper.Argb.getArgb(0, 0, 0, 0);

    private final MinecraftClient client;
    private final HexBorderConfig config;

    public HexBorderDebugRenderer(MinecraftClient client) {
        this.client = client;
        this.config = HexBorderMod.CONFIG_MANAGER.get();
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            double cameraX, double cameraY, double cameraZ) {
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();
        RenderSystem.lineWidth(1.0f);

        Tessellator tessellator = Tessellator.getInstance();

        Entity entity = this.client.gameRenderer.getCamera().getFocusedEntity();
        Vec3d pos = entity.getPos();
        Vec3d offset = new Vec3d(this.config.offset.x, 0, this.config.offset.z);
        int side = this.config.side;
        Polygon polygon = new Hexagon(pos, offset, side);

        this.renderMainPolygon(tessellator, polygon, cameraX, cameraY, cameraZ);
        for (int i = 0; i < polygon.getVerticesNumber(); i++) {
            this.renderNeighborPolygon(tessellator, polygon, i, cameraX, cameraY, cameraZ);
        }

        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
    }

    private void renderMainPolygon(Tessellator tessellator, Polygon polygon,
            double cameraX, double cameraY, double cameraZ) {
        Vec2<Integer> index = polygon.getMainOriginIndex();
        HexBorderColors colors = this.config.mainColors;
        this.renderLine(tessellator, polygon, index, colors, cameraX, cameraY, cameraZ);
    }

    private void renderNeighborPolygon(Tessellator tessellator, Polygon polygon, int i,
            double cameraX, double cameraY, double cameraZ) {
        Vec2<Integer> index = polygon.getNeighborOriginIndex(i);
        HexBorderColors colors = this.config.neighborColors;
        this.renderLine(tessellator, polygon, index, colors, cameraX, cameraY, cameraZ);
    }

    private void renderLine(Tessellator tessellator, Polygon polygon, Vec2<Integer> index, HexBorderColors colors,
            double cameraX, double cameraY, double cameraZ) {
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

        Vec3d origin = polygon.getOriginPos(index);

        int worldBottomY = this.client.world.getBottomY();
        int worldTopY = this.client.world.getTopY();
        double bottomY = (double) worldBottomY - cameraY;
        double topY = (double) worldTopY - cameraY;

        double x, y, z;

        // Origin Vertical Line
        x = origin.x - cameraX;
        z = origin.z - cameraZ;
        this.renderVerticalLine(bufferBuilder, colors.originVerticalLine, x, z, bottomY, topY);

        // Vertex Vertical Line
        Vec3d vertices[] = new Vec3d[polygon.getVerticesNumber()];
        for (int j = 0; j < polygon.getVerticesNumber(); j++) {
            vertices[j] = polygon.getVertexPos(j, index);
            x = vertices[j].x - cameraX;
            z = vertices[j].z - cameraZ;
            this.renderVerticalLine(bufferBuilder, colors.vertexVerticalLine, x, z, bottomY, topY);
        }

        // Horizontal Polyline
        for (int h = worldBottomY; h <= worldTopY; h += 4) {
            int sideHorizontalColor = h % 8 == 0 ? colors.sideHorizontalMainLine : colors.sideHorizontalSubLine;
            y = (double) h - cameraY;
            this.renderHorizontalPolyline(bufferBuilder, sideHorizontalColor, y, vertices, cameraX, cameraZ);
        }

        tessellator.draw();
    }

    private void renderVerticalLine(BufferBuilder bufferBuilder, int color, double x, double z,
            double bottomY, double topY) {
        bufferBuilder.vertex(x, bottomY, z).color(TRANSPARENT).next();
        bufferBuilder.vertex(x, bottomY, z).color(color).next();
        bufferBuilder.vertex(x, topY, z).color(color).next();
        bufferBuilder.vertex(x, topY, z).color(TRANSPARENT).next();
    }

    private void renderHorizontalPolyline(BufferBuilder bufferBuilder, int color, double y,
            Vec3d[] vertices, double cameraX, double cameraZ) {
        double x0 = vertices[0].x - cameraX;
        double z0 = vertices[0].z - cameraZ;
        bufferBuilder.vertex(x0, y, z0).color(TRANSPARENT).next();

        for (Vec3d vertex : vertices) {
            double x = vertex.x - cameraX;
            double z = vertex.z - cameraZ;
            bufferBuilder.vertex(x, y, z).color(color).next();
        }

        bufferBuilder.vertex(x0, y, z0).color(color).next();
        bufferBuilder.vertex(x0, y, z0).color(TRANSPARENT).next();
    }
}
