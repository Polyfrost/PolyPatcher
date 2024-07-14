package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
    //#if MC==10809
    WorldRenderer.class
    //#else
    //$$ net.minecraft.client.Minecraft.class
    //#endif
)
public abstract class WorldRendererMixin_GrowBuffer {
    //#if MC==10809
    @Shadow
    protected abstract void growBuffer(int p_181670_1_);

    @Shadow
    private VertexFormat vertexFormat;

    @Redirect(method = "addVertexData", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;growBuffer(I)V"))
    private void patcher$expandBuffer(WorldRenderer instance, int j, int[] vertexData) {
        this.growBuffer(vertexData.length + this.vertexFormat.getNextOffset());
    }
    //#endif
}
