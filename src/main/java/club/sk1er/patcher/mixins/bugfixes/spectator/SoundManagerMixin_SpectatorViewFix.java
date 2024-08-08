package club.sk1er.patcher.mixins.bugfixes.spectator;

import club.sk1er.patcher.ducks.SoundManagerExt;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class SoundManagerMixin_SpectatorViewFix implements SoundManagerExt {
    @Shadow
    private boolean loaded;

    @Shadow
    private SoundManager.SoundSystemStarterThread sndSystem;

    @Inject(method = "setListener", at = @At("HEAD"))
    private void patcher$redirectListener(EntityPlayer player, float p_148615_2_, CallbackInfo ci) {
        patcher$setListenerManager((Entity) player, p_148615_2_);
    }

    @Override
    public void patcher$setListenerManager(Entity player, float partialTicks) {
        if (this.loaded && player != null) {
            float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
            float f1 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
            double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) partialTicks;
            double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) partialTicks + (double)player.getEyeHeight();
            double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTicks;
            float f2 = MathHelper.cos((f1 + 90.0F) * (float) (Math.PI / 180.0));
            float f3 = MathHelper.sin((f1 + 90.0F) * (float) (Math.PI / 180.0));
            float f4 = MathHelper.cos(-f * (float) (Math.PI / 180.0));
            float f5 = MathHelper.sin(-f * (float) (Math.PI / 180.0));
            float f6 = MathHelper.cos((-f + 90.0F) * (float) (Math.PI / 180.0));
            float f7 = MathHelper.sin((-f + 90.0F) * (float) (Math.PI / 180.0));
            float f8 = f2 * f4;
            float f9 = f3 * f4;
            float f10 = f2 * f6;
            float f11 = f3 * f6;
            this.sndSystem.setListenerPosition((float) d0, (float) d1, (float) d2);
            this.sndSystem.setListenerOrientation(f8, f5, f9, f10, f7, f11);
        }
    }
}
