package club.sk1er.patcher.mixins.bugfixes.spectator;

import club.sk1er.patcher.ducks.SoundHandlerExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MinecraftMixin_SpectatorViewFix implements SoundHandlerExt {
    @Shadow
    public Entity getRenderViewEntity() {
        return null;
    }

    @Redirect(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;setListener(Lnet/minecraft/entity/player/EntityPlayer;F)V"))
    private void patcher$renderViewEntityListener(SoundHandler instance, EntityPlayer player, float f) {
        patcher$setListenerHandler(this.getRenderViewEntity(), f);
    }

    @Override
    public void patcher$setListenerHandler(Entity entity, float partialTicks) {

    }
}
