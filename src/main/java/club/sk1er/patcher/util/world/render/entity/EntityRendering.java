package club.sk1er.patcher.util.world.render.entity;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.util.world.render.culling.EntityCulling;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityRendering {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void cancelRendering(RenderLivingEvent.Pre<? extends EntityLivingBase> event) {
        //#if MC==10809
        EntityLivingBase entity = event.entity;
        //#else
        //$$ EntityLivingBase entity = event.getEntity();
        //#endif
        if ((PatcherConfig.disableArmorstands && entity instanceof EntityArmorStand) || (PatcherConfig.disableSemitransparentEntities && entity.isInvisible() && entity instanceof EntityPlayer)) {
            event.setCanceled(true);
        }

        final float entityDistance = entity.getDistanceToEntity(mc.thePlayer);
        if (PatcherConfig.entityRenderDistanceToggle && EntityCulling.shouldPerformCulling) {
            if (entityDistance > PatcherConfig.entityRenderDistance) {
                event.setCanceled(true);
            } else if (entity instanceof IMob && entityDistance > PatcherConfig.hostileEntityRenderDistance) {
                event.setCanceled(true);
            } else if ((entity instanceof EntityAnimal || entity instanceof EntityAmbientCreature || entity instanceof EntityWaterMob) && entityDistance > PatcherConfig.passiveEntityRenderDistance) {
                event.setCanceled(true);
            } else if (entity instanceof EntityPlayer && entityDistance > PatcherConfig.playerRenderDistance) {
                event.setCanceled(true);
            }
        }
    }
}
