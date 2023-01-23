package club.sk1er.patcher.util.keybind;

import cc.polyfrost.oneconfig.events.event.MouseInputEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.Minecraft;

public class MousePerspectiveKeybindHandler {
    @Subscribe
    public void onMouseInput(MouseInputEvent event) {
        // TODO: Ideally fix the issue at its source instead of handling ourselves, couldn't find a good way to do so.
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings.keyBindTogglePerspective.isPressed()) {
            mc.gameSettings.thirdPersonView = (mc.gameSettings.thirdPersonView + 1) % 3;
            mc.renderGlobal.setDisplayListEntitiesDirty();

            if (!PatcherConfig.keepShadersOnPerspectiveChange) {
                if (mc.gameSettings.thirdPersonView == 0) {
                    mc.entityRenderer.loadEntityShader(mc.getRenderViewEntity());
                } else if (mc.gameSettings.thirdPersonView == 1) {
                    mc.entityRenderer.loadEntityShader(null);
                }
            }
        }
    }
}
