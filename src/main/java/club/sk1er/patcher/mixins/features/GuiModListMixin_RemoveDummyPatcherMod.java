package club.sk1er.patcher.mixins.features;

import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Objects;

@Mixin(GuiModList.class)
public class GuiModListMixin_RemoveDummyPatcherMod {

    @Shadow(remap = false)
    private ArrayList<ModContainer> mods;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void patcher$removeDummyPatcherMod(CallbackInfo ci) {
        this.mods.removeIf(modInfo -> Objects.equals(modInfo.getModId(), "patcher"));
    }

    @Inject(method = "reloadMods", at = @At("RETURN"), remap = false)
    private void patcher$reloadMods(CallbackInfo ci) {
        patcher$removeDummyPatcherMod(ci);
    }

}
