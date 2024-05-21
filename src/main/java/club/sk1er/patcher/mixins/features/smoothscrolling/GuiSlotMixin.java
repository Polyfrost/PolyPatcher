package club.sk1er.patcher.mixins.features.smoothscrolling;

import cc.polyfrost.oneconfig.gui.animations.Animation;
import cc.polyfrost.oneconfig.gui.animations.DummyAnimation;
import club.sk1er.patcher.util.animation.EaseOutQuart;
import net.minecraft.client.gui.GuiSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static club.sk1er.patcher.config.PatcherConfig.smoothScrolling;

@Mixin(GuiSlot.class)
public class GuiSlotMixin {

    @Shadow protected float amountScrolled;

    @Unique private Animation animation = new DummyAnimation(0f);

    @Unique private float end = 0f;

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiSlot;bindAmountScrolled()V", shift = At.Shift.AFTER))
    private void setAnimation(CallbackInfo ci) {
        if (!smoothScrolling) return;
        end = amountScrolled;
        if (end != animation.getEnd()) {
            animation = new EaseOutQuart(200, animation.get(), end, false);
        }
        amountScrolled = animation.get();
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    private void reset(int mouseXIn, int mouseYIn, float p_148128_3_, CallbackInfo ci) {
        if (!smoothScrolling) return;
        amountScrolled = end;
    }

}
