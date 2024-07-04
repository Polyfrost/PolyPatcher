package club.sk1er.patcher.mixins.bugfixes;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenBook.class)
public class GuiScreenBookMixin_AddBackground extends GuiScreen {
    @Inject(method = "drawScreen", at = @At("HEAD"))
    private void patcher$addBackground(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (PatcherConfig.bookBackground) this.drawWorldBackground(1);
    }
}
