package club.sk1er.patcher.mixins.performance.forge;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Mixin(value = OBJModel.MaterialLibrary.class, remap = false)
public class OBJModelMixin_UnclosedStreams {
    //#if MC==10809
    @Shadow
    private BufferedReader mtlReader;
    @Shadow
    private InputStreamReader mtlStream;

    @Inject(method = "parseMaterials", at = @At("TAIL"))
    private void patcher$fixMemoryLeak(IResourceManager manager, String path, ResourceLocation from, CallbackInfo ci) {
        IOUtils.closeQuietly(this.mtlReader);
        IOUtils.closeQuietly(this.mtlStream);
    }
    //#endif
}
