package club.sk1er.patcher.hooks;

import club.sk1er.patcher.config.PatcherConfig;
//#if MC==11202
//$$ import net.minecraft.block.state.IBlockState;
//#endif
import net.minecraft.client.Minecraft;

@SuppressWarnings("unused")
public class EntityRendererHook {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static boolean zoomToggled = false;
    private static boolean isBeingHeld = false;
    private static float oldSensitivity;
    public static float lastZoomModifier;

    public static void fixMissingChunks() {
        mc.renderGlobal.setDisplayListEntitiesDirty();
    }

    public static boolean getZoomState(boolean zoomKeyDown) {
        if (zoomKeyDown) {
            if (isBeingHeld) return zoomToggled;
            isBeingHeld = true;
            zoomToggled = !zoomToggled;
        } else {
            isBeingHeld = false;
        }
        return zoomToggled;
    }

    public static void reduceSensitivityWhenZoomStarts() {
        oldSensitivity = mc.gameSettings.mouseSensitivity;
        mc.gameSettings.mouseSensitivity = oldSensitivity * PatcherConfig.customZoomSensitivity;
    }

    public static void reduceSensitivityDynamically(float modifier) {
        if (!PatcherConfig.dynamicZoomSensitivity || !ZoomHook.zoomed) return;
        float sensitivity = oldSensitivity * PatcherConfig.customZoomSensitivity;
        sensitivity *= modifier / lastZoomModifier;
        mc.gameSettings.mouseSensitivity = sensitivity;
    }

    public static void resetSensitivity() {
        mc.gameSettings.mouseSensitivity = oldSensitivity;
    }
}
