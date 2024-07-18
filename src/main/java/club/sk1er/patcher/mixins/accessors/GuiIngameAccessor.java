package club.sk1er.patcher.mixins.accessors;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiIngame.class)
public interface GuiIngameAccessor {
    @Accessor
    String getDisplayedTitle();

    @Accessor
    String getDisplayedSubTitle();

    @Accessor
    void setDisplayedTitle(String title);

    @Accessor
    void setDisplayedSubTitle(String subTitle);

    //#if MC==10809
    @Invoker
    boolean invokeShowCrosshair();
    //#endif

    @Invoker
    void invokeRenderVignette(float lightLevel, ScaledResolution scaledRes);

    @Invoker
    void invokeRenderPumpkinOverlay(ScaledResolution scaledRes);

    // render portal
    @Invoker
    void invokeRenderPortal(float timeInPortal, ScaledResolution scaledRes);
}
