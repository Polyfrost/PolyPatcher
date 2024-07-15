package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.hooks.EarsModHook;
import net.minecraft.client.renderer.ImageBufferDownload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * This mixin is based off of SkyBlockcatia's, which is licensed under the MIT license
 * <a href="https://github.com/SteveKunG/SkyBlockcatia/blob/1.8.9/LICENSE.md">sbc's license</a>
 */
@Mixin(value = ImageBufferDownload.class)
public abstract class ImageBufferDownloadMixin_ImprovedHeadRendering {
    @Shadow
    private int[] imageData;
    @Shadow
    private int imageWidth;
    @Shadow
    private int imageHeight;
    @Shadow
    protected abstract void setAreaOpaque(int x, int y, int width, int height);
    @Shadow
    protected abstract void setAreaTransparent(int x, int y, int width, int height);

    @Inject(method = "parseUserSkin", at = @At("HEAD"), cancellable = true)
    private void patcher$removeTransparentPixels(BufferedImage image, CallbackInfoReturnable<BufferedImage> cir) {
        if (image != null && PatcherConfig.improvedSkinRendering) {
            this.imageWidth = 64;
            this.imageHeight = 64;
            BufferedImage bufferedImage = new BufferedImage(this.imageWidth, this.imageHeight, 2);
            Graphics graphics = bufferedImage.getGraphics();
            graphics.drawImage(image, 0, 0, null);

            if (image.getHeight() == 32) {
                graphics.drawImage(bufferedImage, 24, 48, 20, 52, 4, 16, 8, 20, null);
                graphics.drawImage(bufferedImage, 28, 48, 24, 52, 8, 16, 12, 20, null);
                graphics.drawImage(bufferedImage, 20, 52, 16, 64, 8, 20, 12, 32, null);
                graphics.drawImage(bufferedImage, 24, 52, 20, 64, 4, 20, 8, 32, null);
                graphics.drawImage(bufferedImage, 28, 52, 24, 64, 0, 20, 4, 32, null);
                graphics.drawImage(bufferedImage, 32, 52, 28, 64, 12, 20, 16, 32, null);
                graphics.drawImage(bufferedImage, 40, 48, 36, 52, 44, 16, 48, 20, null);
                graphics.drawImage(bufferedImage, 44, 48, 40, 52, 48, 16, 52, 20, null);
                graphics.drawImage(bufferedImage, 36, 52, 32, 64, 48, 20, 52, 32, null);
                graphics.drawImage(bufferedImage, 40, 52, 36, 64, 44, 20, 48, 32, null);
                graphics.drawImage(bufferedImage, 44, 52, 40, 64, 40, 20, 44, 32, null);
                graphics.drawImage(bufferedImage, 48, 52, 44, 64, 52, 20, 56, 32, null);
                graphics.dispose();
                this.imageData = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
                this.setAreaOpaque(0, 0, 32, 16);
                this.setAreaTransparent(32, 0, 64, 32);
                this.setAreaOpaque(0, 16, 64, 32);
                this.setAreaTransparent(0, 32, 16, 48);
                this.setAreaTransparent(16, 32, 40, 48);
                this.setAreaTransparent(40, 32, 56, 48);
                this.setAreaTransparent(0, 48, 16, 64);
                this.setAreaOpaque(16, 48, 48, 64);
                this.setAreaTransparent(48, 48, 64, 64);
                cir.setReturnValue(bufferedImage);
            } else {
                graphics.dispose();
                this.imageData = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
                cir.setReturnValue(image);
            }
            EarsModHook.preprocessSkin((ImageBufferDownload) (Object) this, image, bufferedImage);
        }
    }

    @Inject(method = "parseUserSkin", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void patcher$callPreprocess(BufferedImage image, CallbackInfoReturnable<BufferedImage> cir, BufferedImage bufferedImage
        //#if MC<=10809
        , Graphics var3
        //#endif
    ) {
        if (!PatcherConfig.improvedSkinRendering) {
            EarsModHook.preprocessSkin((ImageBufferDownload) (Object) this, image, bufferedImage);
        }
    }

}
