package club.sk1er.patcher.mixins.bugfixes.levelhead;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "club.sk1er.mods.levelhead.render.AboveHeadRender")
public class AboveHeadRender_ReFixNametagPerspective {

    @Dynamic("Levelhead")
    @ModifyArg(method = "renderName", at = @org.spongepowered.asm.mixin.injection.At(value = "INVOKE", target = "Lgg/essential/universal/UGraphics$GL;rotate(FFFF)V", ordinal = 1), index = 0)
    private float patcher$fixNametagPerspective(float f) {
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        if (gameSettings != null && gameSettings.thirdPersonView == 2) {
            return -f;
        }
        return f;
    }
}
