package club.sk1er.patcher.mixins.features;

import net.minecraft.client.renderer.ImageBufferDownload;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.image.BufferedImage;

@Mixin(ImageBufferDownload.class)
public class ImageBufferDownload_EarsMod {

    @Dynamic("Ears Mod")
    @Redirect(method = "parseUserSkin", at = @At(value = "INVOKE", target = "Lcom/unascribed/ears/Ears;preprocessSkin(Lnet/minecraft/client/renderer/ImageBufferDownload;Ljava/awt/image/BufferedImage;Ljava/awt/image/BufferedImage;)V", remap = false), remap = true)
    private void preprocessSkin(ImageBufferDownload subject, BufferedImage rawImg, BufferedImage img) {
        // this will never work because the way they find the locals is really really fragile and doesn't work with this mixin
    }
}
