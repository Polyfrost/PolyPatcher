package club.sk1er.patcher.mixins.performance.forge;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SidedInvWrapper.class, remap = false)
public class SidedInvWrapperMixin_ImproveInsertion {
    //#if MC==10809
    @Shadow
    @Final
    protected ISidedInventory inv;
    @Shadow
    @Final
    protected EnumFacing side;

    @Inject(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemHandlerHelper;canItemStacksStack(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", shift = At.Shift.BEFORE), cancellable = true)
    private void patcher$earlyExit(int slot, ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stackInSlot = this.inv.getStackInSlot(slot);
        if (stackInSlot.stackSize >= Math.min(stackInSlot.getMaxStackSize(), this.inv.getInventoryStackLimit())) {
            cir.setReturnValue(stack);
        }
    }

    @Redirect(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/ISidedInventory;isItemValidForSlot(ILnet/minecraft/item/ItemStack;)Z"))
    private boolean patcher$cancelEarlyInsert(ISidedInventory instance, int i, ItemStack itemStack) {
        return true;
    }

    @Redirect(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/ISidedInventory;canInsertItem(ILnet/minecraft/item/ItemStack;Lnet/minecraft/util/EnumFacing;)Z"))
    private boolean patcher$cancelEarlyInsert2(ISidedInventory instance, int i, ItemStack itemStack, EnumFacing enumFacing) {
        return true;
    }

    @Inject(method = "insertItem", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I", shift = At.Shift.BEFORE), cancellable = true)
    private void patcher$newInsert(int slot, ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        if (!this.inv.canInsertItem(slot, stack, this.side) || !this.inv.isItemValidForSlot(slot, stack)) {
            cir.setReturnValue(stack);
        }
    }
    //#endif
}
