package club.sk1er.patcher.mixins.performance.forge;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(value = ASMDataTable.class, remap = false)
public class ASMDataTableMixin_ComputeParallel {
    //#if MC==10809
    @Shadow
    private Map<ModContainer, SetMultimap<String, ASMDataTable.ASMData>> containerAnnotationData;

    @Shadow
    private List<ModContainer> containers;

    @Shadow
    private SetMultimap<String, ASMDataTable.ASMData> globalAnnotationData;

    @Inject(method = "getAnnotationsFor", at = @At("HEAD"))
    private void computeParallel(ModContainer container, CallbackInfoReturnable<SetMultimap<String, ASMDataTable.ASMData>> cir) {
        if (containerAnnotationData == null) {
            //concurrently filter the values to speed this up
            containerAnnotationData = containers.parallelStream()
                .map(cont -> Pair.of(cont, ImmutableSetMultimap.copyOf(Multimaps.filterValues(globalAnnotationData, data -> cont.getSource().equals(data.getCandidate().getModContainer())))))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
            containerAnnotationData = ImmutableMap.copyOf(containerAnnotationData);
        }
    }
    //#endif
}
