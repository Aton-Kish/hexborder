package atonkish.hexborder.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.debug.DebugRenderer.Renderer;
import net.minecraft.client.util.math.MatrixStack;

import atonkish.hexborder.client.render.debug.DebugRendererAccessor;
import atonkish.hexborder.client.render.debug.HexBorderDebugRenderer;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin implements DebugRendererAccessor {
    @Final
    public Renderer hexBorderDebugRenderer;

    private boolean showHexBorder;

    @Override
    public boolean toggleShowHexBorder() {
        this.showHexBorder = !this.showHexBorder;
        return this.showHexBorder;
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    public void DebugRenderer(MinecraftClient client, CallbackInfo ci) {
        this.hexBorderDebugRenderer = new HexBorderDebugRenderer(client);
    }

    @Inject(at = @At("TAIL"), method = "reset")
    public void reset(CallbackInfo ci) {
        this.hexBorderDebugRenderer.clear();
    }

    @Inject(at = @At("HEAD"), method = "render")
    public void render(MatrixStack matrices, Immediate vertexConsumers,
            double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (this.showHexBorder && !MinecraftClient.getInstance().hasReducedDebugInfo()) {
            this.hexBorderDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        }
    }
}
