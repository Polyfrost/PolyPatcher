package club.sk1er.patcher.util.world.sound;

import cc.polyfrost.oneconfig.config.elements.BasicOption;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.patcher.events.SoundPlayEvent;
import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.config.PatcherSoundConfig;
import club.sk1er.patcher.mixins.accessors.PositionedSoundAccessor;
import club.sk1er.patcher.mixins.accessors.SoundHandlerAccessor;
import club.sk1er.patcher.mixins.accessors.SoundRegistryAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;

import java.util.HashMap;
import java.util.Map;

public class SoundHandler implements IResourceManagerReloadListener {

    public SoundHandler() {
        this.previousActive = Display.isActive();
        handleFocusChange();
    }

    private final Map<ResourceLocation, BasicOption> data = new HashMap<>();

    @Subscribe
    public void onSound(SoundPlayEvent event) {
        ISound soundResult = event.sound;
        if (soundResult instanceof PositionedSoundAccessor) {
            PositionedSoundAccessor result = (PositionedSoundAccessor) soundResult;

            result.setVolume(result.getVolumeField() * getVolumeMultiplier(soundResult.getSoundLocation()));
        }
    }

    private boolean previousActive;
    private float previousVolume = -1f;

    @Subscribe
    public void onTick(TickEvent event) {
        if (event.stage != Stage.START) return;
        boolean active = Display.isActive();
        if (active != previousActive) {
            previousActive = active;
            handleFocusChange();
        }
    }

    private void handleFocusChange() {
        if (!previousActive) {
            SoundManager soundManager = ((SoundHandlerAccessor) Minecraft.getMinecraft().getSoundHandler()).getSndManager();
            previousVolume = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.MASTER);
            if (previousVolume == -1f) return;
            soundManager.setSoundCategoryVolume(SoundCategory.MASTER, PatcherConfig.unfocusedSounds * previousVolume);
        } else {
            ((SoundHandlerAccessor) Minecraft.getMinecraft().getSoundHandler()).getSndManager().setSoundCategoryVolume(SoundCategory.MASTER, previousVolume);
            previousVolume = -1f;
        }
    }

    private float getVolumeMultiplier(ResourceLocation sound) {
        BasicOption propertyData = data.get(sound);
        if (propertyData != null) {
            Object asAny;
            try {
                asAny = propertyData.get();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (asAny instanceof Integer) return ((Integer) asAny).floatValue() / 100F;
        }
        return 1.0f;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        Map<ResourceLocation, SoundEventAccessorComposite> soundRegistry = ((SoundRegistryAccessor) ((SoundHandlerAccessor) Minecraft.getMinecraft().getSoundHandler()).getSndRegistry()).getSoundRegistry();
        new PatcherSoundConfig(data, soundRegistry);
    }
}
