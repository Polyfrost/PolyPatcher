package club.sk1er.patcher.mixins.bugfixes.gui;

import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenBook.class)
public class GuiScreenBookMixin {

    @Inject(method = "drawScreen", at = @At(value = "HEAD"))
    private void alpha(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

    @Inject(method = "drawScreen", at = @At(value = "TAIL"))
    private void end(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }

}
