package club.sk1er.patcher.mixins.bugfixes;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FontRenderer.class)
public class FontRendererMixin_ResetBlend {
    @Inject(method = "renderString", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V", shift = At.Shift.BEFORE))
    private void patcher$renderStringPre(String text, float x, float y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> cir) {
        GlStateManager.enableBlend();
    }

}
