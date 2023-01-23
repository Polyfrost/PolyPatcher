package club.sk1er.patcher.events;

import net.minecraft.client.renderer.RenderGlobal;

public class WorldPostRenderEvent {
    public final RenderGlobal renderGlobal;
    public final float deltaTicks;

    public WorldPostRenderEvent(RenderGlobal renderGlobal, float deltaTicks) {
        this.renderGlobal = renderGlobal;
        this.deltaTicks = deltaTicks;
    }
}
