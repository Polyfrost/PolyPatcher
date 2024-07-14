package club.sk1er.patcher.mixins.performance.forge;

import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.b3d.B3DLoader;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.InputStream;

@Mixin(value = B3DLoader.class, remap = false)
public class B3DLoaderMixin_UnclosedStream {
    @Unique
    private IResource patcher$leakingResource;

    // for some reason localcapture doesn't work here, so let's just store it locally like this
    @Redirect(method = "loadModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/IResource;getInputStream()Ljava/io/InputStream;"))
    private InputStream patcher$getStream(IResource instance) {
        this.patcher$leakingResource = instance;
        return instance.getInputStream();
    }

    @Inject(method = "loadModel", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private void patcher$closeStream(ResourceLocation modelLocation, CallbackInfoReturnable<IModel> cir) {
        IOUtils.closeQuietly(this.patcher$leakingResource.getInputStream());
    }
}
