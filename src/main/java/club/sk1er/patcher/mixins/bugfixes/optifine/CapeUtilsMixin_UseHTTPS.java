package club.sk1er.patcher.mixins.bugfixes.optifine;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Pseudo
@Mixin(targets = "net.optifine.player.CapeUtils", remap = false)
public class CapeUtilsMixin_UseHTTPS {
    @Dynamic("OptiFine")
    @ModifyConstant(
        method = "downloadCape", constant = @Constant(stringValue = "http://s.optifine.net/capes/")
    )
    private String useHTTPS(String url) {
        return "https://optifine.net/capes/";
    }
}
