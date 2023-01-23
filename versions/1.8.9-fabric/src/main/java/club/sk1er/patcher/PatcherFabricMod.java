package club.sk1er.patcher;

import net.fabricmc.api.ClientModInitializer;
import net.legacyfabric.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class PatcherFabricMod implements ClientModInitializer {
    public PatcherFabricMod() {
        new Patcher();
    }

    @Override
    public void onInitializeClient() {
        System.out.println("Post init");
        Patcher.instance.onInit(it -> it.forEach(KeyBindingHelper::registerKeyBinding));
        Patcher.instance.onPostInit();
        Patcher.instance.onLoadComplete();
    }
}
