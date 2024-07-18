package club.sk1er.patcher.mixins.performance.forge;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
//#if MC==10809
import net.minecraft.client.resources.model.ModelResourceLocation;
//#endif
import net.minecraft.item.Item;
import net.minecraftforge.client.ItemModelMesherForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ItemModelMesherForge.class)
public class ItemModelMesherForgeMixin_FastUtil extends ItemModelMesher {
    public ItemModelMesherForgeMixin_FastUtil(ModelManager modelManager) {
        super(modelManager);
    }

    //#if MC==10809
    @Unique
    final Map<Item, Int2ObjectMap<ModelResourceLocation>> patcher$locations = Maps.newIdentityHashMap();
    @Unique
    final Map<Item, Int2ObjectMap<IBakedModel>> patcher$models = Maps.newIdentityHashMap();

    @Inject(method = "getItemModel", at = @At("HEAD"), cancellable = true)
    private void patcher$fastUtilGetModel(Item item, int meta, CallbackInfoReturnable<IBakedModel> cir) {
        Int2ObjectMap<IBakedModel> map = patcher$models.get(item);
        cir.setReturnValue(map == null ? null : map.get(meta));
    }

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private void patcher$fastUtilRegisterModel(Item item, int meta, ModelResourceLocation location, CallbackInfo ci) {
        Int2ObjectMap<ModelResourceLocation> locs = patcher$locations.get(item);
        Int2ObjectMap<IBakedModel>           mods = patcher$models.get(item);
        if (locs == null) {
            locs = new Int2ObjectOpenHashMap<>();
            patcher$locations.put(item, locs);
        }
        if (mods == null) {
            mods = new Int2ObjectOpenHashMap<>();
            patcher$models.put(item, mods);
        }
        locs.put(meta, location);
        mods.put(meta, this.getModelManager().getModel(location));
        ci.cancel();
    }

    @Inject(method = "rebuildCache", at = @At("HEAD"), cancellable = true)
    private void patcher$fastUtilRebuildCache(CallbackInfo ci) {
        final ModelManager manager = this.getModelManager();
            for (Map.Entry<Item, Int2ObjectMap<ModelResourceLocation>> e : patcher$locations.entrySet()) {
            Int2ObjectMap<IBakedModel> mods = patcher$models.get(e.getKey());
            if (mods != null) {
                mods.clear();
            }
            else {
                mods = new Int2ObjectOpenHashMap<>();
                patcher$models.put(e.getKey(), mods);
            }
            final Int2ObjectMap<IBakedModel> map = mods;
            for (Int2ObjectMap.Entry<ModelResourceLocation> entry : e.getValue().int2ObjectEntrySet()) {
                map.put(entry.getIntKey(), manager.getModel(entry.getValue()));
            }
        }
        ci.cancel();
    }
    //#endif
}
