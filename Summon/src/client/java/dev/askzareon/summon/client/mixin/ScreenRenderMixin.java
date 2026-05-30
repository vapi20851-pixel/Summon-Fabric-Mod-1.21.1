package dev.askzareon.summon.client.mixin;

import dev.askzareon.summon.State;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class ScreenRenderMixin {
    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    private void summon$bg(GuiGraphics g, int mx, int my, float d, CallbackInfo ci) {
        if (!State.dead()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPanorama", at = @At("HEAD"), cancellable = true)
    private void summon$pan(GuiGraphics g, float d, CallbackInfo ci) {
        if (!State.dead()) {
            ci.cancel();
        }
    }
}
