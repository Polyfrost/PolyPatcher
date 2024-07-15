package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.hooks.ZoomHook;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryPlayer.class)
public class InventoryPlayerTransformer_HotbarScrolling {
    @Shadow
    public int currentItem;

    @Inject(method = "changeCurrentItem", at = @At("HEAD"), cancellable = true)
    private void patcher$cancelScrolling(int direction, CallbackInfo ci) {
        if (PatcherConfig.disableHotbarScrolling || (PatcherConfig.scrollToZoom && ZoomHook.zoomed)) {
            ci.cancel();
        }
    }

    @Inject(method = "changeCurrentItem", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    private void patcher$invertScrolling(int direction, CallbackInfo ci) {
        if (PatcherConfig.invertHotbarScrolling) {
            int dir;
            if (direction < 0) {
                dir = -2;
            } else {
                dir = 2;
            }
            this.currentItem += dir;
        }
    }

    @ModifyConstant(method = "changeCurrentItem", constant = {@Constant(intValue = 9, ordinal = 0), @Constant(intValue = 9, ordinal = 2)})
    private int patcher$preventOverflowScrolling(int constant) {
        if (PatcherConfig.preventOverflowHotbarScrolling) {
            return 1;
        }

        return constant;
    }
}
