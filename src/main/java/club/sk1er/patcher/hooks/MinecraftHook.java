package club.sk1er.patcher.hooks;

import club.sk1er.patcher.mixins.accessors.KeyBindingAccessor;
import club.sk1er.patcher.screen.render.overlay.metrics.MetricsData;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@SuppressWarnings("unused")
public class MinecraftHook {
    public static MetricsData metricsData;

    //#if MC==10809
    public static void updateKeyBindState() {
        for (KeyBinding keybinding : KeyBindingAccessor.getKeybindArray()) {
            try {
                final int keyCode = keybinding.getKeyCode();
                KeyBinding.setKeyBindState(keyCode, keyCode < 256 && Keyboard.isKeyDown(keyCode));
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
    }
    //#endif

}
