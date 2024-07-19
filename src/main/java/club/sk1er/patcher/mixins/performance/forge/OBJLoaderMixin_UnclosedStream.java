package club.sk1er.patcher.mixins.performance.forge;

import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = OBJLoader.class, remap = false)
public class OBJLoaderMixin_UnclosedStream {
    //#if MC==10809
    @Unique
    private IResource patcher$resource;

    // for some reason localcapture doesn't work here, so let's just store it locally like this
    @ModifyArgs(method = "loadModel", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/model/obj/OBJModel$Parser;<init>(Lnet/minecraft/client/resources/IResource;Lnet/minecraft/client/resources/IResourceManager;)V"))
    private void patcher$getStream(Args args) {
        this.patcher$resource = args.get(0);
    }

    @Inject(method = "loadModel", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private void patcher$closeStream(ResourceLocation modelLocation, CallbackInfoReturnable<IModel> cir) {
        IOUtils.closeQuietly(this.patcher$resource.getInputStream());
    }
    //#endif
}
