package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.hooks.VertexLighterFlatHook;
import com.google.common.cache.LoadingCache;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static net.minecraftforge.client.model.pipeline.LightUtil.mapFormats;

@Mixin(value = LightUtil.class, remap = false)
public abstract class LightUtilMixin_OptimizeNormals {
    //#if MC==10809
    @Unique
    private static final ConcurrentMap<Pair<VertexFormat, VertexFormat>, int[]> patcher$FORMAT_MAPS = new ConcurrentHashMap<>();
    @Unique
    private static final VertexFormat patcher$DEFAULT_FROM = VertexLighterFlatHook.withNormal(DefaultVertexFormats.BLOCK);
    @Unique
    private static final VertexFormat patcher$DEFAULT_TO = DefaultVertexFormats.ITEM;
    @Unique
    private static final int[] patcher$DEFAULT_MAPPING = patcher$generateMapping(patcher$DEFAULT_FROM, patcher$DEFAULT_TO);

    @SuppressWarnings("UnstableApiUsage")
    @Redirect(method = "putBakedQuad", at = @At(value = "INVOKE", target = "Lcom/google/common/cache/LoadingCache;getUnchecked(Ljava/lang/Object;)Ljava/lang/Object;"))
    private static <K, V> V patcher$getUnchecked(LoadingCache<K, V> instance, K k) {
        return (V) mapFormats((VertexFormat) k, DefaultVertexFormats.ITEM);
    }

//    @Inject(method = "mapFormats", at = @At("HEAD"), cancellable = true)
//    private static void patcher$fasterMapFormats(VertexFormat from, VertexFormat to, CallbackInfoReturnable<int[]> cir) {
//        if (from.equals(patcher$DEFAULT_FROM) && to.equals(patcher$DEFAULT_TO)) {
//            cir.setReturnValue(patcher$DEFAULT_MAPPING);
//            return;
//        }
//        cir.setReturnValue(patcher$FORMAT_MAPS.computeIfAbsent(Pair.of(from, to), pair -> patcher$generateMapping(pair.getLeft(), pair.getRight())));
//    }

    /**
     * @author MicrocontrollersDev
     * @reason the above inject doesnt work
     */
    @Overwrite
    public static int[] mapFormats(VertexFormat from, VertexFormat to) {
        if (from.equals(patcher$DEFAULT_FROM) && to.equals(patcher$DEFAULT_TO)) return patcher$DEFAULT_MAPPING;
        return patcher$FORMAT_MAPS.computeIfAbsent(Pair.of(from, to), pair -> patcher$generateMapping(pair.getLeft(), pair.getRight()));
    }

    @Unique
    private static int[] patcher$generateMapping(VertexFormat from, VertexFormat to) {
        int fromCount = from.getElementCount();
        int toCount = to.getElementCount();
        int[] eMap = new int[fromCount];

        for(int e = 0; e < fromCount; e++) {
            VertexFormatElement expected = from.getElement(e);
            int e2;
            for(e2 = 0; e2 < toCount; e2++) {
                VertexFormatElement current = to.getElement(e2);
                if(expected.getUsage() == current.getUsage() && expected.getIndex() == current.getIndex()) {
                    break;
                }
            }
            eMap[e] = e2;
        }
        return eMap;
    }
    //#endif
}
