package club.sk1er.patcher.util.item;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class TooltipHandler {

    public static TooltipHandler INSTANCE = new TooltipHandler();

    public boolean shouldToolTipRender = false;
    private long cacheTime = 0L;
    public List<String> tooltipCache = null;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (!PatcherConfig.tooltipCache) {
                return;
            }
            if (!shouldToolTipRender || tooltipCache == null) {
                return;
            }
            if ((System.currentTimeMillis() - cacheTime) < 200) {
                return;
            }
            tooltipCache = null;
            cacheTime = System.currentTimeMillis();
        }
    }

}
