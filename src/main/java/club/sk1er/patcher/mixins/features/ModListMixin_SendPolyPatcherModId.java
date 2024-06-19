package club.sk1er.patcher.mixins.features;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FMLHandshakeMessage.ModList.class)
public class ModListMixin_SendPolyPatcherModId {

    @ModifyArg(method = "<init>(Ljava/util/List;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), index = 0, remap = false)
    private Object replaceMod(Object key) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            return key;
        }
        if (key instanceof String && key.equals("patcher")) {
            return "polypatcher";
        } else {
            return key;
        }
    }
}
