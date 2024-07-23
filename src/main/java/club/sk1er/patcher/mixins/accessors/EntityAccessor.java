package club.sk1er.patcher.mixins.accessors;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {
    //#if MC==11202
    //$$ @Accessor
    //$$ boolean isGlowing();
    //#endif
}
