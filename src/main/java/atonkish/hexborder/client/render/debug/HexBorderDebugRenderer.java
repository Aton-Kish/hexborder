package atonkish.hexborder.client.render.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer.Renderer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(value = EnvType.CLIENT)
public class HexBorderDebugRenderer implements Renderer {
    public HexBorderDebugRenderer(MinecraftClient client) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            double cameraX, double cameraY, double cameraZ) {
    }
}
