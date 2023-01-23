package club.sk1er.patcher.util.keybind;

import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * Used for dropping entire stacks on computers that don't allow for doing so, such as macOS.
 */
public class KeybindDropModifier extends KeyBinding {

    private final Minecraft mc = Minecraft.getMinecraft();

    public KeybindDropModifier() {
        super("Drop Stack Modifier", Keyboard.KEY_NONE, "Patcher");
    }

    @Subscribe
    public void tick(TickEvent event) {
        if (event.stage == Stage.START) {
            final EntityPlayerSP player = mc.thePlayer;
            if (player != null && !player.isSpectator() && GameSettings.isKeyDown(this) && GameSettings.isKeyDown(mc.gameSettings.keyBindDrop) && mc.currentScreen == null) {
                player.dropOneItem(true);
            }
        }
    }
}
