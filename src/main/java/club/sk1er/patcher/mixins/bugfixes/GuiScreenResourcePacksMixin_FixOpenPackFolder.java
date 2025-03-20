package club.sk1er.patcher.mixins.bugfixes;

import dev.deftu.omnicore.client.OmniDesktop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenResourcePacks.class)
public class GuiScreenResourcePacksMixin_FixOpenPackFolder {

    @Inject(
        method = "actionPerformed",
        at =
        @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/ResourcePackRepository;getDirResourcepacks()Ljava/io/File;",
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void patcher$fixFolderOpening(CallbackInfo ci) {
        if (OmniDesktop.open(Minecraft.getMinecraft().getResourcePackRepository().getDirResourcepacks())) {
            ci.cancel();
        }
    }

}
