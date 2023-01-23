//#if FORGE==1
package club.sk1er.patcher;

import club.sk1er.patcher.hooks.EntityRendererHook;
import club.sk1er.patcher.screen.PatcherMenuEditor;
import club.sk1er.patcher.screen.render.caching.HUDCaching;
import club.sk1er.patcher.screen.render.overlay.ArmorStatusRenderer;
import club.sk1er.patcher.screen.render.overlay.GlanceRenderer;
import club.sk1er.patcher.screen.render.overlay.ImagePreview;
import club.sk1er.patcher.screen.render.overlay.OverlayHandler;
import club.sk1er.patcher.screen.render.overlay.metrics.MetricsRenderer;
import club.sk1er.patcher.screen.render.title.TitleFix;
import club.sk1er.patcher.util.chat.ChatHandler;
import club.sk1er.patcher.util.fov.FovHandler;
import club.sk1er.patcher.util.keybind.MousePerspectiveKeybindHandler;
import club.sk1er.patcher.util.keybind.linux.LinuxKeybindFix;
import club.sk1er.patcher.util.world.render.culling.EntityCulling;
import club.sk1er.patcher.util.world.render.entity.EntityRendering;
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
            Patcher.instance.getSoundHandler(), Patcher.instance.getAudioSwitcher(),
            new OverlayHandler(), new EntityRendering(), new FovHandler(),
            new ChatHandler(), new GlanceRenderer(), new EntityCulling(),
            new ArmorStatusRenderer(), new PatcherMenuEditor(), new ImagePreview(),
            new TitleFix(), new LinuxKeybindFix(),
            new MetricsRenderer(), new HUDCaching(), new EntityRendererHook(),
            new MousePerspectiveKeybindHandler()
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
