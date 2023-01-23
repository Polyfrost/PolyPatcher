package club.sk1er.patcher.events;

import net.minecraft.client.audio.ISound;

public class SoundPlayEvent {
    public ISound sound;

    public SoundPlayEvent(ISound sound) {
        this.sound = sound;
    }
}
