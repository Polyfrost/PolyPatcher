package club.sk1er.patcher.hooks;

import com.unascribed.ears.Ears;
import net.minecraft.client.renderer.ImageBufferDownload;

import java.awt.image.BufferedImage;

public class EarsModHook {
    private static boolean isEarsModPresent;
    private static boolean isEarsModPresentChecked;
    public static void preprocessSkin(ImageBufferDownload imageBufferDownload, BufferedImage image, BufferedImage bufferedImage) {
        if (!isEarsModPresentChecked) {
            isEarsModPresentChecked = true;
            try {
                Class<?> clazz = Class.forName("com.unascribed.ears.Ears");
                clazz.getMethod("preprocessSkin", ImageBufferDownload.class, BufferedImage.class, BufferedImage.class);
                isEarsModPresent = true;
            } catch (ClassNotFoundException e) {
                isEarsModPresent = false;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                isEarsModPresent = false;
            }
        }
        if (!isEarsModPresent) return;
        Ears.preprocessSkin(imageBufferDownload, image, bufferedImage);
    }
}
