package club.sk1er.patcher.hooks;

import gg.essential.api.EssentialAPI;

public class ScreenshotManagerHook {
    public static boolean isEssentialScreenshot() {
        return EssentialAPI.getConfig().getEssentialScreenshots();
    }
}
