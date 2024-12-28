package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiLanguage.class)
public class GuiLanguageMixin_ResetUnicodeFont extends GuiScreen {

    @Override
    public void onGuiClosed() {
        this.mc.ingameGUI.getChatGUI().refreshChat();
    }
}
