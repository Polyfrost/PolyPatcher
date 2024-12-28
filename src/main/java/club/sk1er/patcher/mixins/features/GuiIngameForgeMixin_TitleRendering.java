package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import org.polyfrost.universal.UResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngameForge.class, remap = false)
public class GuiIngameForgeMixin_TitleRendering extends GuiIngame {

    @Shadow
    private FontRenderer fontrenderer;

    public GuiIngameForgeMixin_TitleRendering(Minecraft mc) {
        super(mc);
    }

    @Inject(method = "renderTitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V", ordinal = 0, shift = At.Shift.AFTER, remap = true))
    private void patcher$modifyTitle(int l, int age, float opacity, CallbackInfo ci) {
        float titleScale = 1;
        if (PatcherConfig.autoTitleScale) {
            float width = this.fontrenderer.getStringWidth(this.displayedTitle) * 4.0F;
            if (width > UResolution.getScaledWidth()) {
                titleScale = (UResolution.getScaledWidth() / width);
            }
        }
        GlStateManager.scale(titleScale, titleScale, titleScale);
    }
    @Inject(method = "renderTitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V", ordinal = 1, shift = At.Shift.AFTER, remap = true))
    private void patcher$modifySubtitle(int l, int age, float opacity, CallbackInfo ci) {
        float titleScale = 1;
        if (PatcherConfig.autoTitleScale) {
            float width = this.fontrenderer.getStringWidth(this.displayedSubTitle) * 2.0F;
            if (width > UResolution.getScaledWidth()) {
                titleScale = (UResolution.getScaledWidth() / width);
            }
        }
        GlStateManager.scale(titleScale, titleScale, titleScale);
    }
}
