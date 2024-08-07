package club.sk1er.patcher.util.chat;

import org.polyfrost.oneconfig.api.ui.v1.NotificationsManager;
import org.polyfrost.polyui.notify.Notifications;
import org.polyfrost.universal.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;

public class ChatUtilities {

    public static void sendMessage(String message) {
        sendMessage(message, true);
    }

    public static void sendMessage(String message, boolean prefix) {
        sendMessageHelper(prefix ? translate("&e[Patcher] &r") + translate(message) : translate(message));
    }

    private static void sendMessageHelper(String message) {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if (player != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(translate(message)));
        }
    }

    public static void sendNotification(String notificationCategory, String chatMessage) { //TODO replace this individually :sob:
        //Notifications.INSTANCE.enqueue(notificationCategory, translate(chatMessage));
    }

    public static String translate(String message) {
        return ChatColor.Companion.translateAlternateColorCodes('&', message);
    }
}
