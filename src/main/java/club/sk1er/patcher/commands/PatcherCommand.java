package club.sk1er.patcher.commands;

import dev.deftu.textile.minecraft.MCSimpleTextHolder;
import dev.deftu.textile.minecraft.MCTextFormat;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.*;
import club.sk1er.patcher.Patcher;
import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.util.chat.ChatUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.jetbrains.annotations.Nullable;
import org.polyfrost.oneconfig.utils.v1.dsl.ScreensKt;

@Command(value = {"patcher", "polypatcher"})
public class PatcherCommand {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final int randomBound = 85673;
    public static int randomChatMessageId;

    @Handler
    private static void main() {
        ScreensKt.openUI(Patcher.instance.getPatcherConfig());
    }

    @Handler(value = "blacklist", description = "Tell the client that you don't want to use the 1.11+ chat length on the specified server IP.")
    public void blacklist(@Param("ip") String ip) {
        String status = Patcher.instance.addOrRemoveBlacklist(ip) ? "&cnow" : "&ano longer";
        ChatUtilities.sendNotification(
            "Server Blacklist",
            "Server &e\"" + ip + "\" &ris " + status + " &rblacklisted from chat length extension."
        );
        Patcher.instance.saveBlacklistedServers();
    }

    @Handler(value = "fov", description = "Change your FOV to a custom value.")
    public void fov(@Param("amount") float amount) {
        if (amount <= 0) {
            ChatUtilities.sendNotification("FOV Changer", "Changing your FOV to or below 0 is disabled due to game-breaking visual bugs.");
            return;
        } else if (amount > 110) {
            ChatUtilities.sendNotification("FOV Changer", "Changing your FOV above 110 is disabled due to game-breaking visual bugs.");
            return;
        }

        ChatUtilities.sendNotification(
            "FOV Changer",
            "FOV changed from &e" + mc.gameSettings.fovSetting + "&r to &a" + amount + "."
        );
        mc.gameSettings.fovSetting = amount;
        mc.gameSettings.saveOptions();
    }

    @Handler(value = "sendcoords", description = "Send your current coordinates in chat. Anything after 'sendcoords' will be put at the end of the message.")
    public void sendcoords(@Param("additional information") @Nullable String message) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        player.sendChatMessage("x: " + (int) player.posX + ", y: " + (int) player.posY + ", z: " + (int) player.posZ +
            // might be an issue if they provide a long message?
            " " + ((message == null) ? "" : message));
    }
}
