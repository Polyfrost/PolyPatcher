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
        if (keyCode == mc.gameSettings.keyBindInventory.getKeyCode()) {
            mc.thePlayer.closeScreen();
        }
        //#if MC==10809
        if (theSlot != null && theSlot.getHasStack()) {
            if (keyCode == mc.gameSettings.keyBindPickBlock.getKeyCode()) {
                handleMouseClick(theSlot, theSlot.slotNumber, 0, 3);
            } else if (keyCode == mc.gameSettings.keyBindDrop.getKeyCode()) {
                handleMouseClick(theSlot, theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
            }
        }
        //#else
        //$$ if (hoveredSlot != null && hoveredSlot.getHasStack()) {
        //$$    if (mc.gameSettings.keyBindPickBlock.isActiveAndMatches(keyCode)) {
        //$$        handleMouseClick(hoveredSlot, hoveredSlot.slotNumber, 0, ClickType.CLONE);
        //$$    } else if (mc.gameSettings.keyBindDrop.isActiveAndMatches(keyCode)) {
        //$$        handleMouseClick(hoveredSlot, hoveredSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
        //$$    }
        //$$ }
        //#endif
    }
}
