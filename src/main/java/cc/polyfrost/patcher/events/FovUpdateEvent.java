package cc.polyfrost.patcher.events;

import net.minecraft.entity.player.EntityPlayer;

public class FovUpdateEvent {
    public final EntityPlayer entity;
    public float fov;

    public FovUpdateEvent(EntityPlayer entity, float fov) {
        this.entity = entity;
        this.fov = fov;
    }
}
