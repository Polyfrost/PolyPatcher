package club.sk1er.patcher.mixins.bugfixes;

import club.sk1er.patcher.ducks.GameSettingsExt;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiVideoSettings.class)
public abstract class GuiVideoSettingsMixin_MipmapSlider extends GuiScreen {
    //#if MC==10809
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ((GameSettingsExt) this.mc.gameSettings).patcher$onSettingsGuiClosed();
    }
    //#endif
}
