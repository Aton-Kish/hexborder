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

import atonkish.hexborder.util.math.Vec2;
import atonkish.hexborder.util.polygon.Hexagon;
import atonkish.hexborder.util.polygon.Polygon;

@Environment(value = EnvType.CLIENT)
public class HexBorderDebugRenderer implements Renderer {
    private static final int TRANSPARENT_COLOR = ColorHelper.Argb.getArgb(0, 0, 0, 0);
    private static final int ORIGIN_VERTICAL_COLOR = ColorHelper.Argb.getArgb(128, 0, 0, 255);
    private static final int VERTEX_VERTICAL_COLOR = ColorHelper.Argb.getArgb(128, 255, 0, 0);
    private static final int SIDE_HORIZONTAL_MAIN_COLOR = ColorHelper.Argb.getArgb(128, 0, 155, 155);
    private static final int SIDE_HORIZONTAL_SUB_COLOR = ColorHelper.Argb.getArgb(128, 255, 255, 0);

    private final MinecraftClient client;

    public HexBorderDebugRenderer(MinecraftClient client) {
        this.client = client;
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
        Polygon polygon = new Hexagon(pos);

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
        this.renderLine(tessellator, polygon, index, cameraX, cameraY, cameraZ);
    }

    private void renderNeighborPolygon(Tessellator tessellator, Polygon polygon, int i,
            double cameraX, double cameraY, double cameraZ) {
        Vec2<Integer> index = polygon.getNeighborOriginIndex(i);
        this.renderLine(tessellator, polygon, index, cameraX, cameraY, cameraZ);
    }

    private void renderLine(Tessellator tessellator, Polygon polygon, Vec2<Integer> index,
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
        this.renderVerticalLine(bufferBuilder, ORIGIN_VERTICAL_COLOR, x, z, bottomY, topY);

        // Vertex Vertical Line
        Vec3d vertices[] = new Vec3d[polygon.getVerticesNumber()];
        for (int j = 0; j < polygon.getVerticesNumber(); j++) {
            vertices[j] = polygon.getVertexPos(j, index);
            x = vertices[j].x - cameraX;
            z = vertices[j].z - cameraZ;
            this.renderVerticalLine(bufferBuilder, VERTEX_VERTICAL_COLOR, x, z, bottomY, topY);
        }

        // Horizontal Polyline
        for (int h = worldBottomY; h <= worldTopY; h += 4) {
            int sideHorizontalColor = h % 8 == 0 ? SIDE_HORIZONTAL_MAIN_COLOR : SIDE_HORIZONTAL_SUB_COLOR;
            y = (double) h - cameraY;
            this.renderHorizontalPolyline(bufferBuilder, sideHorizontalColor, y, vertices, cameraX, cameraZ);
        }

        tessellator.draw();
    }

    private void renderVerticalLine(BufferBuilder bufferBuilder, int color, double x, double z,
            double bottomY, double topY) {
        bufferBuilder.vertex(x, bottomY, z).color(TRANSPARENT_COLOR).next();
        bufferBuilder.vertex(x, bottomY, z).color(color).next();
        bufferBuilder.vertex(x, topY, z).color(color).next();
        bufferBuilder.vertex(x, topY, z).color(TRANSPARENT_COLOR).next();
    }

    private void renderHorizontalPolyline(BufferBuilder bufferBuilder, int color, double y,
            Vec3d[] vertices, double cameraX, double cameraZ) {
        double x0 = vertices[0].x - cameraX;
        double z0 = vertices[0].z - cameraZ;
        bufferBuilder.vertex(x0, y, z0).color(TRANSPARENT_COLOR).next();

        for (Vec3d vertex : vertices) {
            double x = vertex.x - cameraX;
            double z = vertex.z - cameraZ;
            bufferBuilder.vertex(x, y, z).color(color).next();
        }

        bufferBuilder.vertex(x0, y, z0).color(color).next();
        bufferBuilder.vertex(x0, y, z0).color(TRANSPARENT_COLOR).next();
    }
}
