package club.sk1er.patcher.events;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;

public class LivingEntityRenderEvent<T extends EntityLivingBase> {
    public final EntityLivingBase entity;
    public final RendererLivingEntity<T> renderer;
    public final double x;
    public final double y;
    public final double z;

    public LivingEntityRenderEvent(EntityLivingBase entity, RendererLivingEntity<T> renderer, double x, double y, double z) {
        this.entity = entity;
        this.renderer = renderer;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static class Pre<T extends EntityLivingBase> extends LivingEntityRenderEvent<T> {
        public boolean isCancelled = false;
        public Pre(EntityLivingBase entity, RendererLivingEntity<T> renderer, double x, double y, double z) {
            super(entity, renderer, x, y, z);
        }
    }

    public static class Post<T extends EntityLivingBase> extends LivingEntityRenderEvent<T> {
        public Post(EntityLivingBase entity, RendererLivingEntity<T> renderer, double x, double y, double z) {
            super(entity, renderer, x, y, z);
        }
    }
}
