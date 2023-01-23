package club.sk1er.patcher.mixins.bugfixes;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(TileEntityBannerRenderer.class)
public class TileEntityBannerRendererMixin_ChestDisplay {
    //#if MC==10809
    @Shadow @Final private static Map<String, TileEntityBannerRenderer.TimedBannerTexture> DESIGNS;

    @Shadow @Final private static ResourceLocation BANNERTEXTURES;

    /**
     * @author asbyth
     * @reason Resolve banners in chests not displaying once cache is full
     */
    @Overwrite
    private ResourceLocation func_178463_a(TileEntityBanner banner) {
        String texture = banner.getPatternResourceLocation();

        if (texture.isEmpty()) {
            return null;
        } else {
            TileEntityBannerRenderer.TimedBannerTexture timedTexture = TileEntityBannerRendererMixin_ChestDisplay.DESIGNS.get(texture);
            if (timedTexture == null) {
                if (TileEntityBannerRendererMixin_ChestDisplay.DESIGNS.size() >= 256 && !this.patcher$freeCacheSlot()) {
                    return TileEntityBannerRendererMixin_ChestDisplay.BANNERTEXTURES;
                }

                List<TileEntityBanner.EnumBannerPattern> patternList = banner.getPatternList();
                List<EnumDyeColor> colorList = banner.getColorList();
                List<String> patternPath = Lists.newArrayList();

                for (TileEntityBanner.EnumBannerPattern pattern : patternList) {
                    patternPath.add("textures/entity/banner/" + pattern.getPatternName() + ".png");
                }

                timedTexture = new TileEntityBannerRenderer.TimedBannerTexture();
                timedTexture.bannerTexture = new ResourceLocation(texture);
                Minecraft.getMinecraft().getTextureManager().loadTexture(timedTexture.bannerTexture, new LayeredColorMaskTexture(TileEntityBannerRendererMixin_ChestDisplay.BANNERTEXTURES, patternPath, colorList));
                TileEntityBannerRendererMixin_ChestDisplay.DESIGNS.put(texture, timedTexture);
            }

            timedTexture.systemTime = System.currentTimeMillis();
            return timedTexture.bannerTexture;
        }
    }

    @Unique
    private boolean patcher$freeCacheSlot() {
        long start = System.currentTimeMillis();
        Iterator<String> iterator = TileEntityBannerRendererMixin_ChestDisplay.DESIGNS.keySet().iterator();

        while (iterator.hasNext()) {
            String next = iterator.next();
            TileEntityBannerRenderer.TimedBannerTexture timedTexture = TileEntityBannerRendererMixin_ChestDisplay.DESIGNS.get(next);

            if ((start - timedTexture.systemTime) > 5000L) {
                Minecraft.getMinecraft().getTextureManager().deleteTexture(timedTexture.bannerTexture);
                iterator.remove();
                return true;
            }
        }

        return TileEntityBannerRendererMixin_ChestDisplay.DESIGNS.size() < 256;
    }
    //#endif
}
