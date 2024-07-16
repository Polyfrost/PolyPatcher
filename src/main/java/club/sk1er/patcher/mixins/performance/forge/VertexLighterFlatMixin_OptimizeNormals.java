package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.hooks.VertexLighterFlatHook;
import club.sk1er.patcher.hooks.accessors.IVertexLighterFlat;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.QuadGatheringTransformer;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = VertexLighterFlat.class, remap = false)
public abstract class VertexLighterFlatMixin_OptimizeNormals extends QuadGatheringTransformer implements IVertexLighterFlat {
    //#if MC==10809
    @Unique
    protected VertexFormat patcher$baseFormat;
    @Shadow
    protected int posIndex;
    @Shadow
    protected int normalIndex;
    @Shadow
    protected int colorIndex;
    @Shadow
    protected int lightmapIndex;

    @Inject(method = "setParent", at = @At("HEAD"), cancellable = true)
    private void patcher$optimizeSetParent(IVertexConsumer parent, CallbackInfo ci) {
        super.setParent(parent);
        setVertexFormat(parent.getVertexFormat());
        ci.cancel();
    }

    @Override
    public void setVertexFormat(VertexFormat format) {
        if (Objects.equals(format,patcher$baseFormat)) return;
        this.patcher$baseFormat = format;
        super.setVertexFormat(VertexLighterFlatHook.withNormal(format));
        for (int i = 0; i < getVertexFormat().getElementCount(); i++) {
            switch (getVertexFormat().getElement(i).getUsage()) {
                case POSITION:
                    posIndex = i;
                    break;
                case NORMAL:
                    normalIndex = i;
                    break;
                case COLOR:
                    colorIndex = i;
                    break;
                case UV:
                    if (getVertexFormat().getElement(i).getIndex() == 1) {
                        lightmapIndex = i;
                    }
                    break;
                default:
            }
        }
        if (posIndex == -1) {
            throw new IllegalArgumentException("vertex lighter needs format with position");
        }
        if (lightmapIndex == -1) {
            throw new IllegalArgumentException("vertex lighter needs format with lightmap");
        }
        if (colorIndex == -1) {
            throw new IllegalArgumentException("vertex lighter needs format with color");
        }
    }
    //#endif
}
