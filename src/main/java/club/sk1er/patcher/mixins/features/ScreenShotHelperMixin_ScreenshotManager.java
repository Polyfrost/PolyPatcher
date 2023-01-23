package club.sk1er.patcher.mixins.features;

import cc.polyfrost.oneconfig.utils.Multithreading;
import club.sk1er.patcher.Patcher;
import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.util.screenshot.AsyncScreenshots;
import gg.essential.api.EssentialAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.nio.IntBuffer;

@Mixin(ScreenShotHelper.class)
public class ScreenShotHelperMixin_ScreenshotManager {

    @Shadow private static IntBuffer pixelBuffer;
    @Shadow private static int[] pixelValues;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Inject(method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;", at = @At("HEAD"), cancellable = true)
    private static void patcher$screenshotManager(File gameDirectory, String screenshotName, int width, int height, Framebuffer framebuffer, CallbackInfoReturnable<IChatComponent> cir) {
        if (PatcherConfig.screenshotManager && (!Patcher.instance.isEssential() || EssentialAPI.getConfig().getEssentialScreenshots())) {
            File screenshotDirectory = new File(Minecraft.getMinecraft().mcDataDir, "screenshots");
            if (!screenshotDirectory.exists()) {
                screenshotDirectory.mkdir();
            }

            if (OpenGlHelper.isFramebufferEnabled()) {
                width = framebuffer.framebufferTextureWidth;
                height = framebuffer.framebufferTextureHeight;
            }

            int scale = width * height;

            IntBuffer buffer = ScreenShotHelperMixin_ScreenshotManager.pixelBuffer;
            int[] values = ScreenShotHelperMixin_ScreenshotManager.pixelValues;

            if (buffer == null || buffer.capacity() < scale) {
                buffer = BufferUtils.createIntBuffer(scale);
                values = new int[scale];
            }

            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            buffer.clear();

            if (OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.bindTexture(framebuffer.framebufferTexture);
                GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
            } else {
                GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
            }

            buffer.get(values);
            Multithreading.runAsync(new AsyncScreenshots(width, height, values, screenshotDirectory));

            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            if (player != null && !PatcherConfig.screenshotNoFeedback) {
                player.addChatMessage(new ChatComponentText(" "));
            }

            cir.setReturnValue(new ChatComponentText(AsyncScreenshots.prefix + "Capturing screenshot."));

            ScreenShotHelperMixin_ScreenshotManager.pixelBuffer = buffer;
            ScreenShotHelperMixin_ScreenshotManager.pixelValues = values;
        }
    }
}
