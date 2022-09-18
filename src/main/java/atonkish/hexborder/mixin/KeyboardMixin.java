package atonkish.hexborder.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.text.Text;

import atonkish.hexborder.HexBorderConfig;
import atonkish.hexborder.HexBorderMod;
import atonkish.hexborder.client.render.debug.DebugRendererAccessor;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    abstract public void debugLog(Text text);

    @Shadow
    abstract public void debugLog(String key, Object... args);

    @Inject(method = "processF3", at = @At("HEAD"), cancellable = true)
    public void processF3(final int key, final CallbackInfoReturnable<Boolean> cir) {
        HexBorderConfig config = HexBorderMod.CONFIG_MANAGER.get();
        Key keyCode = config.f3KeyBindings.toggleShowBorderKey.getKeyCode();
        if (key == keyCode.getCode()) {
            boolean bl2 = ((DebugRendererAccessor) this.client.debugRenderer).toggleShowHexBorder();
            this.debugLog(bl2 ? "debug.hexagon_boundaries.on" : "debug.hexagon_boundaries.off", new Object[0]);
            cir.setReturnValue(true);
            return;
        }
    }
}
