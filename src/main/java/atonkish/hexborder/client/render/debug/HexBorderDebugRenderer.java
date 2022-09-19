package atonkish.hexborder.client.render.debug;

import java.util.List;
import java.util.Optional;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import atonkish.hexborder.HexBorderConfig;
import atonkish.hexborder.HexBorderMod;
import atonkish.hexborder.HexBorderConfig.HexBorderColors;
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

        Tessellator tessellator = Tessellator.getInstance();

        World world = this.client.world;
        Vec3d playerPos = this.client.player.getPos();
        Vec3d offset = new Vec3d(this.config.offset.x, 0, this.config.offset.z);
        int side = this.config.side;
        Polygon polygon = new Hexagon(playerPos, offset, side);

        this.renderPolygon(tessellator, this.config.mainColors, polygon, world, playerPos, cameraX, cameraY, cameraZ);
        for (int i = 0; i < polygon.getVerticesNumber(); i++) {
            this.renderPolygon(tessellator, this.config.neighborColors, polygon.getNeighbor(i), world, playerPos,
                    cameraX, cameraY, cameraZ);
        }

        RenderSystem.lineWidth(1.0f);
        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
    }

    private void renderPolygon(Tessellator tessellator, HexBorderColors colors, Polygon polygon,
            World world, Vec3d playerPos, double cameraX, double cameraY, double cameraZ) {
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        Vec3d origin = polygon.getOriginPos().add(-cameraX, 0, -cameraZ);
        Vec3d vertices[] = new Vec3d[polygon.getVerticesNumber()];
        for (int i = 0; i < polygon.getVerticesNumber(); i++) {
            vertices[i] = polygon.getVertexPos(i).add(-cameraX, 0, -cameraZ);
        }
        List<Vec3d> areaPointList = polygon.getAreaPointList();

        int worldBottomY = world.getBottomY();
        int worldTopY = world.getTopY();
        double bottomY = (double) worldBottomY - cameraY;
        double topY = (double) worldTopY - cameraY;

        RenderSystem.lineWidth(1.0f);
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

        // Origin Vertical Line
        this.renderVerticalLine(bufferBuilder, colors.originVerticalLine, origin.x, origin.z, bottomY, topY);

        // Vertex Vertical Line
        for (Vec3d vertex : vertices) {
            this.renderVerticalLine(bufferBuilder, colors.vertexVerticalLine, vertex.x, vertex.z, bottomY, topY);
        }

        // Horizontal Polyline
        for (int h = worldBottomY; h <= worldTopY; h += 4) {
            int sideHorizontalColor = h % 8 == 0 ? colors.sideHorizontalMainLine : colors.sideHorizontalSubLine;
            double y = (double) h - cameraY;
            this.renderHorizontalPolyline(bufferBuilder, sideHorizontalColor, y, vertices);
        }

        tessellator.draw();

        RenderSystem.lineWidth(2.0f);
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

        this.renderGrid(bufferBuilder, colors.grid, world, playerPos, areaPointList, cameraX, cameraY, cameraZ);

        tessellator.draw();
    }

    private void renderVerticalLine(BufferBuilder bufferBuilder, int color, double x, double z,
            double bottomY, double topY) {
        int alpha = ColorHelper.Argb.getAlpha(color);
        if (alpha == 0) {
            return;
        }

        bufferBuilder.vertex(x, bottomY, z).color(TRANSPARENT).next();
        bufferBuilder.vertex(x, bottomY, z).color(color).next();
        bufferBuilder.vertex(x, topY, z).color(color).next();
        bufferBuilder.vertex(x, topY, z).color(TRANSPARENT).next();
    }

    private void renderHorizontalPolyline(BufferBuilder bufferBuilder, int color, double y, Vec3d[] vertices) {
        int alpha = ColorHelper.Argb.getAlpha(color);
        if (alpha == 0) {
            return;
        }

        bufferBuilder.vertex(vertices[0].x, y, vertices[0].z).color(TRANSPARENT).next();

        for (Vec3d vertex : vertices) {
            bufferBuilder.vertex(vertex.x, y, vertex.z).color(color).next();
        }

        bufferBuilder.vertex(vertices[0].x, y, vertices[0].z).color(color).next();
        bufferBuilder.vertex(vertices[0].x, y, vertices[0].z).color(TRANSPARENT).next();
    }

    private void renderGrid(BufferBuilder bufferBuilder, int color, World world, Vec3d playerPos, List<Vec3d> areaPointList,
            double cameraX, double cameraY, double cameraZ) {
        int alpha = ColorHelper.Argb.getAlpha(color);
        if (alpha == 0) {
            return;
        }

        for (Vec3d point : areaPointList) {
            Optional<Double> gridY = this.retrieveGridY(world, playerPos, point);
            if (gridY.isEmpty()) {
                continue;
            }

            Vec3d gridPos = new Vec3d(point.x, gridY.get() + 0.05D, point.z).add(-cameraX, -cameraY, -cameraZ);

            bufferBuilder.vertex(gridPos.x, gridPos.y, gridPos.z).color(TRANSPARENT).next();
            bufferBuilder.vertex(gridPos.x, gridPos.y, gridPos.z).color(color).next();
            bufferBuilder.vertex(gridPos.x + 1, gridPos.y, gridPos.z).color(color).next();
            bufferBuilder.vertex(gridPos.x + 1, gridPos.y, gridPos.z + 1).color(color).next();
            bufferBuilder.vertex(gridPos.x, gridPos.y, gridPos.z + 1).color(color).next();
            bufferBuilder.vertex(gridPos.x, gridPos.y, gridPos.z).color(color).next();
            bufferBuilder.vertex(gridPos.x, gridPos.y, gridPos.z).color(TRANSPARENT).next();

            bufferBuilder.vertex(gridPos.x, gridPos.y, gridPos.z).color(TRANSPARENT).next();
            bufferBuilder.vertex(gridPos.x, gridPos.y, gridPos.z).color(color).next();
            bufferBuilder.vertex(gridPos.x + 1, gridPos.y, gridPos.z + 1).color(color).next();
            bufferBuilder.vertex(gridPos.x + 1, gridPos.y, gridPos.z + 1).color(TRANSPARENT).next();

            bufferBuilder.vertex(gridPos.x + 1, gridPos.y, gridPos.z).color(TRANSPARENT).next();
            bufferBuilder.vertex(gridPos.x + 1, gridPos.y, gridPos.z).color(color).next();
            bufferBuilder.vertex(gridPos.x, gridPos.y, gridPos.z + 1).color(color).next();
            bufferBuilder.vertex(gridPos.x, gridPos.y, gridPos.z + 1).color(TRANSPARENT).next();
        }
    }

    private Optional<Double> retrieveGridY(World world, Vec3d playerPos, Vec3d gridPos) {
        Vec3d retriever = new Vec3d(gridPos.x, playerPos.y + 1, gridPos.z);

        while (playerPos.distanceTo(retriever) < this.config.viewDistance) {
            BlockPos gridBlockPos = new BlockPos(retriever);
            BlockPos footBlockPos = gridBlockPos.down();

            VoxelShape gridShape = world.getBlockState(gridBlockPos).getCollisionShape(world, gridBlockPos);
            VoxelShape footShape = world.getBlockState(footBlockPos).getCollisionShape(world, footBlockPos);

            if ((gridShape.isEmpty() || gridShape.getMax(Axis.Y) < 1.0) && footShape.getMax(Axis.Y) == 1.0) {
                return Optional.of((double) gridBlockPos.getY());
            }

            retriever = retriever.add(0, -1, 0);
        }

        return Optional.empty();
    }
}
