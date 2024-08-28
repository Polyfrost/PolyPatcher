package club.sk1er.patcher.util.pause;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

public class PauseHandler {
    private int ticks;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Display.isActive()) {
            ticks = 2;
        } else if (ticks > 0) {
            ticks--;
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiMainMenu) {
            ticks = 2;
        }
    }

    @SubscribeEvent
    public void onGuiMouse(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (ticks > 0 && (event.gui != null && PatcherConfig.dontClickIntoGui)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onGameMouse(MouseEvent event) {
        if (ticks > 0) {
            event.setCanceled(true);
            Minecraft.getMinecraft().mouseHelper.mouseXYChange();
        }
    }
}
