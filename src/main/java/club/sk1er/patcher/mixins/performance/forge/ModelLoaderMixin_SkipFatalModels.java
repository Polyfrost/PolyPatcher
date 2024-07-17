package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.ducks.FMLClientHandlerExt;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.model.IBakedModel;
//#if MC==10809
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.IRegistry;
//#endif
import net.minecraftforge.client.model.ModelLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin_SkipFatalModels
    //#if MC==10809
    extends ModelBakery implements FMLClientHandlerExt
    //#endif
{
    //#if MC==10809
    public ModelLoaderMixin_SkipFatalModels(IResourceManager p_i46085_1_, TextureMap p_i46085_2_, BlockModelShapes p_i46085_3_) {
        super(p_i46085_1_, p_i46085_2_, p_i46085_3_);
    }
    //#endif

    //#if MC==10809
    @Inject(method = "setupModelRegistry", at = @At("HEAD"), cancellable = true)
    private void patcher$earlyExit(CallbackInfoReturnable<IRegistry<ModelResourceLocation, IBakedModel>> cir) {
        if (patcher$hasErrors()) cir.setReturnValue(this.bakedRegistry);
    }
    //#endif
}
