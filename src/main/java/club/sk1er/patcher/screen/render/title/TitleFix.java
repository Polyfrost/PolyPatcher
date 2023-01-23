package club.sk1er.patcher.screen.render.title;

import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import club.sk1er.patcher.events.ServerLeaveEvent;
import club.sk1er.patcher.mixins.accessors.GuiIngameAccessor;
import net.minecraft.client.Minecraft;

public class TitleFix {

    @Subscribe
    public void disconnectEvent(ServerLeaveEvent event) {
        GuiIngameAccessor gui = (GuiIngameAccessor) Minecraft.getMinecraft().ingameGUI;

        // these are never cleared when logging out of a server while displaying a title or subtitle,
        // so clear these if they're not already cleared when leaving the server.
        if (!gui.getDisplayedTitle().isEmpty()) gui.setDisplayedTitle("");
        if (!gui.getDisplayedSubTitle().isEmpty()) gui.setDisplayedSubTitle("");
    }
}
