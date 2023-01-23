package club.sk1er.patcher.util.keybind.linux;

import cc.polyfrost.oneconfig.events.event.KeyInputEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import club.sk1er.patcher.config.PatcherConfig;
import cc.polyfrost.patcher.events.ScreenEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

//#if MC==11202
//$$ import net.minecraft.inventory.ClickType;
//#endif

public class LinuxKeybindFix {

    private final Minecraft mc = Minecraft.getMinecraft();
    private static final HashMap<Integer, HashMap<Character, Integer>> triggers = new HashMap<Integer, HashMap<Character, Integer>>() {{
        put(1, new HashMap<Character, Integer>() {{ // BE AZERTY
            put('&', 0);
            put('é', 1);
            put('"', 2);
            put('\'', 3);
            put('(', 4);
            put('§', 5);
            put('è', 6);
            put('!', 7);
            put('ç', 8);
        }});
        put(2, new HashMap<Character, Integer>() {{ // FR AZERTY
            put('&', 0);
            put('é', 1);
            put('"', 2);
            put('\'', 3);
            put('(', 4);
            put('-', 5);
            put('è', 6);
            put('_', 7);
            put('ç', 8);
        }});
    }};

    @Subscribe
    public void onKeyPress(KeyInputEvent event) {
        if (SystemUtils.IS_OS_LINUX && mc.thePlayer != null && Keyboard.isCreated() && Keyboard.getEventKeyState()) {
            if (PatcherConfig.keyboardLayout == 0) {
                final int eventKey = Keyboard.getEventKey();
                switch (eventKey) {
                    case 145:
                        if (mc.gameSettings.keyBindsHotbar[1].getKeyCode() == 3) mc.thePlayer.inventory.currentItem = 1;
                        break;

                    case 144:
                        if (mc.gameSettings.keyBindsHotbar[5].getKeyCode() == 7) mc.thePlayer.inventory.currentItem = 5;
                        break;
                }
            } else {
                char charPressed = Keyboard.getEventCharacter();
                if (triggers.get(PatcherConfig.keyboardLayout).containsKey(charPressed)) {
                    int i = triggers.get(PatcherConfig.keyboardLayout).get(charPressed);
                    if (mc.gameSettings.keyBindsHotbar[i].getKeyCode() == i + 2) mc.thePlayer.inventory.currentItem = i;
                }
            }
        }
    }

    @Subscribe
    public void onGuiPress(ScreenEvent.KeyEvent.Pre event) {
        GuiScreen guiScreen = event.screen;
        if (SystemUtils.IS_OS_LINUX && PatcherConfig.keyboardLayout != 0 && guiScreen instanceof GuiContainer && mc.thePlayer != null
            && Keyboard.isCreated() && Keyboard.getEventKeyState()) {
            char charPressed = Keyboard.getEventCharacter();
            GuiContainer gui = (GuiContainer) guiScreen;
            Slot slot = gui.getSlotUnderMouse();
            if (slot != null && triggers.get(PatcherConfig.keyboardLayout).containsKey(charPressed)) {
                mc.playerController.windowClick(
                    gui.inventorySlots.windowId,
                    slot.slotNumber,
                    triggers.get(PatcherConfig.keyboardLayout).get(charPressed),
                    //#if MC==10809
                    2,
                    //#else
                    //$$ ClickType.SWAP,
                    //#endif
                    mc.thePlayer
                );
                event.isCancelled = true;
            }
        }
    }
}
