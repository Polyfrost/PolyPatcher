package club.sk1er.patcher.mixins.performance.hudcaching;

import club.sk1er.patcher.screen.render.caching.HUDCaching;
//import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
//import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
//import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class, priority = 1001)
public class EntityRendererMixin_HUDCaching {
    //@Shadow
    //@Final
    //private Minecraft mc;

    @Redirect(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V"))
    public void patcher$renderCachedHUD(GuiIngame guiIngame, float partialTicks) {
        HUDCaching.renderCachedHud((EntityRenderer) (Object) this, guiIngame, partialTicks);
    }

    //@Redirect(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/ForgeHooksClient;drawScreen(Lnet/minecraft/client/gui/GuiScreen;IIF)V"))
    //private void patcher$cacheScreen(GuiScreen screen, int mouseX, int mouseY, float partialTicks) {
    //    HUDCaching.renderCachedScreen((EntityRenderer) (Object) this, screen, mouseX, mouseY, partialTicks);
    //}
}
