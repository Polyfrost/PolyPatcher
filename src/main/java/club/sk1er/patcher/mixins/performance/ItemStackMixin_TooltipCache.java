package club.sk1er.patcher.mixins.performance;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.util.item.TooltipHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin_TooltipCache {
    @Inject(
            method = "getTooltip",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    public void patcher$useCachedTooltip(EntityPlayer playerIn, boolean advanced, CallbackInfoReturnable<List<String>> cir) {
        if (!PatcherConfig.tooltipCache) {
            return;
        }
        if (TooltipHandler.INSTANCE.tooltipCache != null) {
            TooltipHandler.INSTANCE.shouldToolTipRender = false;
            cir.setReturnValue(TooltipHandler.INSTANCE.tooltipCache);
        }
        TooltipHandler.INSTANCE.shouldToolTipRender = true;
    }

    @Inject(
            method = "getTooltip",
            at = @At(
                    value = "RETURN"
            )
    )
    public void patcher$cacheTooltip(EntityPlayer playerIn, boolean advanced, CallbackInfoReturnable<List<String>> cir) {
        if (!PatcherConfig.tooltipCache) {
            return;
        }
        TooltipHandler.INSTANCE.tooltipCache = cir.getReturnValue();
        TooltipHandler.INSTANCE.shouldToolTipRender = false;
    }
}
