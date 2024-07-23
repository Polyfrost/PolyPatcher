package club.sk1er.patcher.mixins.bugfixes;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.ducks.EntityExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderGlobal.class)
public class RenderGlobalMixin_FixGlow implements EntityExt {
    //#if MC==10809
    @Shadow
    @Final
    private Minecraft mc;

    @Redirect(method = "isRenderEntityOutlines", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isSpectator()Z"))
    private boolean patcher$removeSpectatorCheck(EntityPlayerSP instance) {
        if (PatcherConfig.entityOutlines) return true;
        return instance.isSpectator();
    }

    @Redirect(method = "isRenderEntityOutlines", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    private boolean patcher$removeKeyCheck(KeyBinding instance) {
        if (PatcherConfig.entityOutlines) return true;
        return instance.isKeyDown();
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;shouldRenderInPass(I)Z", ordinal = 1))
    private boolean patcher$forceInPass(Entity instance, int pass, Entity renderViewEntity, ICamera camera, float partialTicks) {
        if (pass == 1) return true;
        else return instance.shouldRenderInPass(pass);
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isInRangeToRender3d(DDD)Z", ordinal = 1))
    private boolean patcher$forceRenderRange(Entity instance, double x, double y, double z, Entity renderViewEntity, ICamera camera, float partialTicks) {
        return instance.isInRangeToRender3d(x, y, z) && patcher$isOutlineActive(instance, renderViewEntity, camera);
    }

    @Unique
    private boolean patcher$isOutlineActive(Entity entityIn, Entity viewer, ICamera camera) {
        boolean flag = viewer instanceof EntityLivingBase && ((EntityLivingBase)viewer).isPlayerSleeping();
        if (entityIn == viewer && this.mc.gameSettings.thirdPersonView == 0 && !flag) {
            return false;
        } else if (((EntityExt) entityIn).patcher$isGlowing() && PatcherConfig.entityOutlines) {
            return true;
        } else {
            return this.mc.thePlayer.isSpectator() && this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown() && entityIn instanceof EntityPlayer
                ? entityIn.ignoreFrustumCheck
                || camera.isBoundingBoxInFrustum(entityIn.getEntityBoundingBox())
                || entityIn.isRiding()
                : false;
        }
    }
    //#endif
}
