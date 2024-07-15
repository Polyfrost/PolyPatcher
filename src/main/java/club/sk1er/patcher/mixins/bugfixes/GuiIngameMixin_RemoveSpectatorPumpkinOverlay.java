package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class GuiIngameMixin_RemoveSpectatorPumpkinOverlay {
    @Inject(method = "renderPumpkinOverlay", at = @At("HEAD"), cancellable = true)
    private void patcher$removeSpectatorPumpkinOverlay(ScaledResolution scaledRes, CallbackInfo ci) {
        if (Minecraft.getMinecraft().thePlayer.isSpectator()) ci.cancel();
    }
}
