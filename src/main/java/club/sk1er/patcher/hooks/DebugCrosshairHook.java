package club.sk1er.patcher.hooks;

import org.polyfrost.universal.UResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class DebugCrosshairHook {

    public static void renderDirections(float partialTicks, Minecraft mc) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(UResolution.getScaledWidth() / 2), (float)(UResolution.getScaledHeight() / 2), 0);
        Entity entity = mc.getRenderViewEntity();
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(-1.0F, -1.0F, -1.0F);
        double mcScale = UResolution.getScaleFactor();
        GlStateManager.scale(1 / mcScale, 1 / mcScale, 1 / mcScale);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        GL11.glLineWidth(2.0F);
        worldRenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        drawLine(worldRenderer, 38, new Color(255, 0, 0), 1, 0, 0);
        drawLine(worldRenderer, 38, new Color(0, 0, 255), 0, 0, 1);
        drawLine(worldRenderer, 27, new Color(0, 255, 0), 0, 1, 0);
        tessellator.draw();

        GL11.glLineWidth(1.0F);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private static void drawLine(WorldRenderer worldRenderer, double length, Color color, int... pos) {
        worldRenderer.pos(0.0, 0.0, 0.0).color(color.getRed(), color.getGreen(), color.getBlue(), 255).endVertex();
        worldRenderer.pos(length * pos[0], length * pos[1], length * pos[2]).color(color.getRed(), color.getGreen(), color.getBlue(), 255).endVertex();
    }

}
