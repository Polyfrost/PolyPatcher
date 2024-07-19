package club.sk1er.patcher.mixins.performance;

import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin_FixEarlyLightInitialization {
    //#if MC==10809
    @Shadow
    public abstract boolean isAreaLoaded(BlockPos par1, int par2, boolean par3);
    @Unique
    private int patcher$range = 17;

    @ModifyConstant(method = "checkLightFor", constant = @Constant(intValue = 17, ordinal = 0))
    private int patcher$modifyRange(int constant) {
        return 16;
    }

    @ModifyConstant(method = "checkLightFor", constant = {@Constant(intValue = 17, ordinal = 0), @Constant(intValue = 17, ordinal = 2)})
    private int patcher$modifyRange2(int constant) {
        return this.patcher$range;
    }

    @Inject(method = "checkLightFor", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", ordinal = 0, shift = At.Shift.BEFORE))
    private void patcher$modifyRange3(EnumSkyBlock lightType, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        this.patcher$range = this.isAreaLoaded(pos, 18, false) ? 17 : 15;
    }
    //#endif
}
