//#if FORGE==1
package club.sk1er.patcher;

import club.sk1er.patcher.screen.render.overlay.GlanceRenderer;
import club.sk1er.patcher.screen.render.overlay.OverlayHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "patcher", name = "Patcher", version = Patcher.VERSION, clientSideOnly = true)
public class PatcherForgeMod {
    public PatcherForgeMod() {
        new Patcher();
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        Patcher.instance.onInit(
            it -> it.forEach(ClientRegistry::registerKeyBinding)
        );

        registerEvents(
            Patcher.instance.getSoundHandler(), new OverlayHandler(),
            new GlanceRenderer()
        );
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        Patcher.instance.onPostInit();
    }

    @Mod.EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        Patcher.instance.onLoadComplete();
    }

    private void registerEvents(Object... events) {
        for (Object event : events) {
            MinecraftForge.EVENT_BUS.register(event);
        }
    }
}
//#endif
