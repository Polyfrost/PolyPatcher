package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.gui.GuiScreenAddServer;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiScreenAddServer.class)
public class GuiScreenAddServerMixin_RemoveSpaces {
    @Redirect(method = "actionPerformed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiTextField;getText()Ljava/lang/String;"))
    private String patcher$removeSpaces(GuiTextField guiTextField) {
        return guiTextField.getText().replaceAll(" ", "");
    }
}
