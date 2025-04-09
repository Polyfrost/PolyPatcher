package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.boss.BossStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiIngame.class)
public class GuiIngameMixin_FixBossbarWidth {
    @Redirect(method = "renderBossHealth", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/boss/BossStatus;healthScale:F"))
    private float patcher$clampBossBarWidth() {
        return Math.min(BossStatus.healthScale, 1.0F);
    }
}
