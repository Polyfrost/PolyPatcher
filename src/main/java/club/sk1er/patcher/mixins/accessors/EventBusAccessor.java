package club.sk1er.patcher.mixins.accessors;

import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EventBus.class)
public interface EventBusAccessor {
    @Accessor
    int getBusID();
}
