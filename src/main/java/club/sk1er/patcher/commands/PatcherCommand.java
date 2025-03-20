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

@Command(value = {"patcher", "polypatcher"})
public class PatcherCommand {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final int randomBound = 85673;
    public static int randomChatMessageId;

    @Command
    private static void main() {
        //todo
        //Patcher.instance.getPatcherConfig().openGui();
    }

    @Command(value = "blacklist", description = "Tell the client that you don't want to use the 1.11+ chat length on the specified server IP.")
    public void blacklist(@Parameter("ip") String ip) {
        String status = Patcher.instance.addOrRemoveBlacklist(ip) ? "&cnow" : "&ano longer";
        ChatUtilities.sendNotification(
            "Server Blacklist",
            "Server &e\"" + ip + "\" &ris " + status + " &rblacklisted from chat length extension."
        );
        Patcher.instance.saveBlacklistedServers();
    }

    @Command(value = "fov", description = "Change your FOV to a custom value.")
    public void fov(@Parameter("amount") float amount) {
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

    @Command(value = {"scale", "invscale", "inventoryscale"}, description = "Change the scale of your inventory independent of your GUI scale.")
    public void scale(@Parameter
        //TODO
                              //(autoCompletesTo = {"help", "off", "none", "small", "normal", "large", "auto", "0", "1", "2", "3", "4", "5"})
                          String argument) {
        if (argument.equalsIgnoreCase("help")) {
            ChatUtilities.sendMessage(new MCSimpleTextHolder("             Inventory Scale").withFormatting(MCTextFormat.YELLOW), false);
            ChatUtilities.sendMessage(new MCSimpleTextHolder("Usage: /inventoryscale <scaling>").withFormatting(MCTextFormat.GRAY), false);
            ChatUtilities.sendMessage(new MCSimpleTextHolder("Scaling may be a number between 1-5, or").withFormatting(MCTextFormat.GRAY), false);
            ChatUtilities.sendMessage(new MCSimpleTextHolder("small/normal/large/auto").withFormatting(MCTextFormat.GRAY), false);
            ChatUtilities.sendMessage(new MCSimpleTextHolder("Use '/inventoryscale off' to disable scaling.").withFormatting(MCTextFormat.GRAY), false);
            return;
        }

        if (argument.equalsIgnoreCase("off") || argument.equalsIgnoreCase("none")) {
            ChatUtilities.sendNotification("Inventory Scale", "Disabled inventory scaling.");
            PatcherConfig.inventoryScale = 0;
            Patcher.instance.forceSaveConfig();
            return;
        }

        int scaling;
        if (argument.equalsIgnoreCase("small")) {
            scaling = 1;
        } else if (argument.equalsIgnoreCase("normal")) {
            scaling = 2;
        } else if (argument.equalsIgnoreCase("large")) {
            scaling = 3;
        } else if (argument.equalsIgnoreCase("auto")) {
            scaling = 5;
        } else {
            try {
                scaling = Integer.parseInt(argument);
            } catch (Exception e) {
                ChatUtilities.sendNotification("Inventory Scale", "Invalid scaling identifier. Use '/patcher scale help' for assistance.");
                return;
            }
        }

        if (scaling < 1) {
            ChatUtilities.sendNotification("Inventory Scale", "Disabled inventory scaling.");
            PatcherConfig.inventoryScale = 0;
            Patcher.instance.forceSaveConfig();
            return;
        } else if (scaling > 5) {
            ChatUtilities.sendNotification("Inventory Scale", "Invalid scaling. Must be between 1-5.");
            return;
        }

        ChatUtilities.sendNotification("Inventory Scale", "Set inventory scaling to " + scaling);
        PatcherConfig.inventoryScale = scaling;
        Patcher.instance.forceSaveConfig();
    }

    @Command(value = "sendcoords", description = "Send your current coordinates in chat. Anything after 'sendcoords' will be put at the end of the message.")
    public void sendcoords(@Parameter("additional information") @Nullable String message) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        player.sendChatMessage("x: " + (int) player.posX + ", y: " + (int) player.posY + ", z: " + (int) player.posZ +
            // might be an issue if they provide a long message?
            " " + ((message == null) ? "" : message));
    }

    @Command(value = "sounds", description = "Open the Sound Configuration GUI.")
    public void sounds() {
        //todo
        //Patcher.instance.getPatcherSoundConfig().openGui();
    }

    @Command(value = "fps", description = "Choose what to limit the game's framerate to outside of Minecraft's options. 0 will use your normal framerate.")
    public void fps(@Parameter("amount") int amount) {
        if (amount < 0) {
            ChatUtilities.sendNotification("Custom FPS Limiter", "You cannot set your framerate to a negative number.");
            return;
        } else if (amount == PatcherConfig.customFpsLimit) {
            ChatUtilities.sendNotification("Custom FPS Limiter", "Custom framerate is already set to this value.");
            return;
        }

        PatcherConfig.customFpsLimit = amount;
        Patcher.instance.forceSaveConfig();

        String message = amount == 0 ? "Custom framerate was reset." : "Custom framerate set to " + amount + ".";
        ChatUtilities.sendNotification("Custom FPS Limiter", message);
    }
}
