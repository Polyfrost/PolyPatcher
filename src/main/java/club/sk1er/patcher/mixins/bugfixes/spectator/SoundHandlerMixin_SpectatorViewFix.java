package club.sk1er.patcher.mixins.bugfixes.spectator;

import club.sk1er.patcher.ducks.SoundHandlerExt;
import club.sk1er.patcher.ducks.SoundManagerExt;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SoundHandler.class)
public class SoundHandlerMixin_SpectatorViewFix implements SoundHandlerExt, SoundManagerExt {
    @Override
    public void patcher$setListenerHandler(Entity entity, float partialTicks) {
        patcher$setListenerManager(entity, partialTicks);
    }

    @Override
    public void patcher$setListenerManager(Entity entity, float partialTicks) {

    }
}
