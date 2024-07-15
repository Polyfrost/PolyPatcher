package club.sk1er.patcher.mixins.bugfixes.render.item;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public abstract class RenderItemMixin_FixGlint {

    @Unique private static final String patcher$RENDER_ITEM_AND_EFFECT_INTO_GUI =
        //#if MC<=10809
        "renderItemAndEffectIntoGUI";
        //#else
        //$$ "renderItemAndEffectIntoGUI(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;II)V";
        //#endif

    @Unique private static final String patcher$RENDER_ITEM_MODEL_INTO_GUI =
        //#if MC<=10809
        "Lnet/minecraft/client/renderer/entity/RenderItem;renderItemIntoGUI(Lnet/minecraft/item/ItemStack;II)V";
        //#else
        //$$ "Lnet/minecraft/client/renderer/RenderItem;renderItemModelIntoGUI(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/renderer/block/model/IBakedModel;)V";
        //#endif

    @Inject(method = patcher$RENDER_ITEM_AND_EFFECT_INTO_GUI, at = @At(value = "INVOKE", target = patcher$RENDER_ITEM_MODEL_INTO_GUI))
    private void patcher$correctGlint(
        //#if MC>10809
        //$$ net.minecraft.entity.EntityLivingBase p_184391_1_,
        //#endif
        ItemStack stack, int xPosition, int yPosition, CallbackInfo ci) {
        GlStateManager.enableDepth();
    }

}
