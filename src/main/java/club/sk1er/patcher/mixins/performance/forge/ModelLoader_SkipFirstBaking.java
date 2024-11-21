package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.Patcher;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.model.IBakedModel;
//#if MC==10809
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.IRegistry;
import net.minecraftforge.client.model.IFlexibleBakedModel;
//#endif
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(value = ModelLoader.class)
public class ModelLoader_SkipFirstBaking
    //#if MC==10809
    extends ModelBakery
    //#endif
{

    //#if MC==10809
    @Shadow(remap = false)
    @Final
    private Map<ModelResourceLocation, IModel> stateModels;
    @Unique
    private static boolean patcher$firstLoad = true;
    @Unique
    private static long patcher$lastTime = 0;

    public ModelLoader_SkipFirstBaking(IResourceManager p_i46085_1_, TextureMap p_i46085_2_, BlockModelShapes p_i46085_3_) {
        super(p_i46085_1_, p_i46085_2_, p_i46085_3_);
    }

    @Inject(method = "setupModelRegistry", at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void patcher$skipFirstBaking(CallbackInfoReturnable<IRegistry<ModelResourceLocation, IBakedModel>> cir, IFlexibleBakedModel missingBaked) {
        if (patcher$firstLoad)
        {
            patcher$firstLoad = false;
            for (ModelResourceLocation mrl : stateModels.keySet())
            {
                bakedRegistry.putObject(mrl, missingBaked);
            }
            cir.setReturnValue(bakedRegistry);
        } else {
            patcher$lastTime = System.currentTimeMillis();
        }
    }

    @Inject(method = "setupModelRegistry", at = @At("RETURN"))
    private void patcher$skipFirstBaking(CallbackInfoReturnable<IRegistry<ModelResourceLocation, IBakedModel>> cir) {
        if (!patcher$firstLoad)
        {
            Patcher.instance.getLogger().info("Saved " + (System.currentTimeMillis() - patcher$lastTime) + "ms by skipping first model baking.");
        }
    }

    //#endif
}
