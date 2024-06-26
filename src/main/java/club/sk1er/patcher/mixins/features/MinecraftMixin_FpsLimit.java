package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin_FpsLimit {
    @Shadow
    public GuiScreen currentScreen;

    @Shadow
    public WorldClient theWorld;

    @Shadow
    public GameSettings gameSettings;

    @Inject(method = "getLimitFramerate", at = @At("HEAD"), cancellable = true)
    private void patcher$modifyFpsLimit(CallbackInfoReturnable<Integer> cir) {
        if (this.theWorld == null && this.currentScreen != null) {
            if (PatcherConfig.smoothScrolling) cir.setReturnValue(gameSettings.limitFramerate);
            return;
        }

        if (!Display.isActive() && PatcherConfig.unfocusedFPS) {
            cir.setReturnValue(2);
        } else if (PatcherConfig.customFpsLimit > 0) {
            cir.setReturnValue(PatcherConfig.customFpsLimit);
        }
    }

    @Inject(method = "isFramerateLimitBelowMax", at = @At("HEAD"), cancellable = true)
    private void patcher$useCustomFrameLimit(CallbackInfoReturnable<Boolean> cir) {
        if (PatcherConfig.customFpsLimit > 0) {
            cir.setReturnValue(true);
        }
    }
}
