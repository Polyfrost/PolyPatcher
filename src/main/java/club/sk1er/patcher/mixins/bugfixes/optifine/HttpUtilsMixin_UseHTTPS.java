package club.sk1er.patcher.mixins.bugfixes.optifine;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Pseudo
@Mixin(targets = "net.optifine.http.HttpUtils", remap = false)
public class HttpUtilsMixin_UseHTTPS {
    @Dynamic("OptiFine")
    @ModifyConstant(
        method = "getPlayerItemsUrl", constant = @Constant(stringValue = "http://s.optifine.net")
    )
    private String useHTTPS(String url) {
        return "https://optifine.net";
    }
}

