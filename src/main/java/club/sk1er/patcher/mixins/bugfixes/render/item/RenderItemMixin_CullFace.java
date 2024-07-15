package club.sk1er.patcher.mixins.bugfixes.render.item;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("deprecation")
@Mixin(RenderItem.class)
public abstract class RenderItemMixin_CullFace {

    //#if MC==10809

    @Shadow protected abstract boolean isThereOneNegativeScale(ItemTransformVec3f itemTranformVec);


    @Inject(method =
        //#if MC==10809
        "renderItemModelTransform"
        //#else
        //$$ "renderItemModel"
        //#endif
        , at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"))
    private void patcher$cullFace(ItemStack stack, IBakedModel model, ItemCameraTransforms.TransformType cameraTransformType, CallbackInfo ci) {
        if (isThereOneNegativeScale(model.getItemCameraTransforms().getTransform(cameraTransformType))) {
            GlStateManager.cullFace(1028);
        }
    }

    //#endif
}
