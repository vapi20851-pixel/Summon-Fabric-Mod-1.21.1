package dev.askzareon.summon.client.mixin;

import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TitleScreen.class)
public interface TitleSplashAccessor {
    @Accessor("splash")
    @Mutable
    void summon$setSplash(String splash);
}
