package club.sk1er.patcher.util.chat;

import dev.deftu.omnicore.client.OmniChat;
import dev.deftu.textile.minecraft.MCSimpleMutableTextHolder;
import dev.deftu.textile.minecraft.MCSimpleTextHolder;
import dev.deftu.textile.minecraft.MCTextFormat;
import dev.deftu.textile.minecraft.MCTextHolder;

public class ChatUtilities {

    public static final MCSimpleTextHolder PREFIX = new MCSimpleTextHolder("[Patcher] ").withFormatting(MCTextFormat.YELLOW);

    public static final char COLOR_CHAR = '&';

    public static void sendMessage(MCTextHolder<?> text) {
        sendMessage(text, true);
    }

    public static void sendMessage(MCTextHolder<?> text, boolean prefix) {
        if (prefix) {
            MCSimpleMutableTextHolder newText = new MCSimpleMutableTextHolder("");
            newText.append(PREFIX);
            newText.append(text);
            text = newText;
        }

        OmniChat.displayClientMessage(text);
    }

    public static void sendNotification(String notificationCategory, String chatMessage) { //TODO replace this individually :sob:
        //Notifications.INSTANCE.enqueue(notificationCategory, translate(chatMessage));
    }

    public static String translate(String text) {
        return MCTextFormat.translateAlternateColorCodes(COLOR_CHAR, text);
    }

}
