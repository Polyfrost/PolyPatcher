package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderManager.class)
public class RenderManagerMixin_PerspectiveViewFix {

    //#if MC==10809
    @Shadow public float playerViewX;

    @Inject(
        method = "cacheActiveRenderInfo",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/renderer/entity/RenderManager;playerViewY:F",
            ordinal = 0,
            shift = At.Shift.AFTER
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                opcode = Opcodes.GETFIELD,
                target = "Lnet/minecraft/client/settings/GameSettings;thirdPersonView:I"
            )
        )
    )

    private void patcher$addCorrectView(World worldIn, FontRenderer textRendererIn, Entity livingPlayerIn, Entity pointedEntityIn, GameSettings optionsIn, float partialTicks, CallbackInfo ci) {
        this.playerViewX = -this.playerViewX;
    }
    //#endif

}
