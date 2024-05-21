package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(GuiScreen.class)
public class GuiScreenMixin_BackgroundRendering {

    @Shadow public Minecraft mc;
    @Shadow public int width;
    @Shadow public int height;

    @Inject(method = "drawDefaultBackground", at = @At("HEAD"), cancellable = true)
    private void patcher$cancelRendering(CallbackInfo ci) {
        if (PatcherConfig.containerBackgroundOpacity == 0 && this.mc.theWorld != null) {
            // Some guis (for example, the advancements gui) depend on the depth buffer having the rectangle from
            // the background in it. So, we draw a similar rectangle to only the depth buffer.
            GlStateManager.colorMask(false, false, false, false);
            Gui.drawRect(0, 0, this.width, this.height, -1);
            GlStateManager.colorMask(true, true, true, true);

            MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.BackgroundDrawnEvent((GuiScreen) (Object) this));
            ci.cancel();
        }
    }

    @ModifyArgs(method = "drawWorldBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;drawGradientRect(IIIIII)V"))
    private void patcher$modifyColor(Args args) {
        if (PatcherConfig.containerBackgroundOpacity != 0) {
            args.set(4, patcher$getOpacity(args.get(4), Math.max(PatcherConfig.containerBackgroundOpacity / 100F - 16/255F, 0)));
            args.set(5, patcher$getOpacity(args.get(4), Math.max(PatcherConfig.containerBackgroundOpacity / 100F, 0)));
        }
    }

    @Unique
    private int patcher$getOpacity(int color, float opacity) {
        return (int) (opacity * 255) << 24 | (color & 0xFFFFFF);
    }
}
