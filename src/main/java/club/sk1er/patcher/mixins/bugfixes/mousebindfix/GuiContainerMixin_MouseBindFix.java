package club.sk1er.patcher.mixins.bugfixes.mousebindfix;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
//#if MC==11202
//$$ import net.minecraft.inventory.ClickType;
//#endif
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public abstract class GuiContainerMixin_MouseBindFix extends GuiScreen {
    //#if MC==10809
    @Shadow
    private Slot theSlot;

    @Shadow
    protected abstract void handleMouseClick(Slot par1, int par2, int par3, int par4);
    //#else
    //$$ @Shadow
    //$$ private Slot hoveredSlot;
    //$$
    //$$ @Shadow
    //$$ protected abstract void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type);
    //#endif

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void patcher$checkCloseClick(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        int keyCode = mouseButton - 100;
        if (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.thePlayer.closeScreen();
        }
        //#if MC==10809
        if (this.theSlot != null && this.theSlot.getHasStack()) {
            if (keyCode == this.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
                this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
            } else if (keyCode == this.mc.gameSettings.keyBindDrop.getKeyCode()) {
                this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, this.isCtrlKeyDown() ? 1 : 0, 4);
            }
        }
        //#else
        //$$ if (this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
        //$$    if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(keyCode)) {
        //$$        this.handleMouseClick(hoveredSlot, this.hoveredSlot.slotNumber, 0, ClickType.CLONE);
        //$$    } else if (this.mc.gameSettings.keyBindDrop.isActiveAndMatches(keyCode)) {
        //$$        this.handleMouseClick(hoveredSlot, this.hoveredSlot.slotNumber, this.isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
        //$$    }
        //$$ }
        //#endif
    }
}
