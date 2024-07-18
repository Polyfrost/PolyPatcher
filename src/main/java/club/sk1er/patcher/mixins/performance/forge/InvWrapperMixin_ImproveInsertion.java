package club.sk1er.patcher.mixins.performance.forge;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(value = InvWrapper.class, remap = false)
public class InvWrapperMixin_ImproveInsertion {
    //#if MC==10809
    @Shadow
    @Final
    public IInventory inv;

    @Inject(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemHandlerHelper;canItemStacksStack(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"), cancellable = true)
    private void patcher$earlyExit(int slot, ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stackInSlot = this.inv.getStackInSlot(slot);
        if (stackInSlot.stackSize >= Math.min(stackInSlot.getMaxStackSize(), this.inv.getInventoryStackLimit())) {
            cir.setReturnValue(stack);
        }
    }

    @Redirect(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/IInventory;isItemValidForSlot(ILnet/minecraft/item/ItemStack;)Z"))
    private boolean patcher$cancelEarlyInsert(IInventory instance, int i, ItemStack itemStack) {
        return true;
    }

    @Inject(method = "insertItem", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I", shift = At.Shift.BEFORE), cancellable = true)
    private void patcher$newInsert(int slot, ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        if (!this.inv.isItemValidForSlot(slot, stack)) {
            cir.setReturnValue(stack);
        }
    }
    //#endif
}
