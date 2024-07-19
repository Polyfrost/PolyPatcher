package club.sk1er.patcher.mixins.bugfixes;

import club.sk1er.patcher.ducks.EntityExt;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin_FixGlow implements EntityExt {
    //#if MC == 10809
    @Shadow
    protected abstract boolean getFlag(int flag);
    @Shadow
    public World worldObj;

    @Shadow
    protected abstract void setFlag(int flag, boolean set);

    @Unique
    private boolean patcher$glowing;

    @Override
    public boolean patcher$isGlowing() {
        return this.patcher$glowing || this.worldObj.isRemote && this.getFlag(6);
    }

    @Unique
    public void patcher$setGlowing(boolean glowingIn) {
        this.patcher$glowing = glowingIn;
        if (!this.worldObj.isRemote) {
            this.setFlag(6, this.patcher$glowing);
        }
    }

    @Inject(method = "readFromNBT", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;)Z", shift = At.Shift.BEFORE))
    private void patcher$setGlowingState(NBTTagCompound tagCompund, CallbackInfo ci) {
        patcher$setGlowing(tagCompund.getBoolean("Glowing"));
    }
    //#endif
}
