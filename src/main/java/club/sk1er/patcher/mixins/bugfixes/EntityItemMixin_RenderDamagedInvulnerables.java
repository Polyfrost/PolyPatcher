package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityItem.class)
public abstract class EntityItemMixin_RenderDamagedInvulnerables
    //#if MC==10809
    extends Entity
    //#endif
{
    //#if MC==10809
    public EntityItemMixin_RenderDamagedInvulnerables(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true)
    private void patcher$properInvulnerableCheck(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.worldObj.isRemote) cir.setReturnValue(false);
    }
    //#endif
}
