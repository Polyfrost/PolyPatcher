package club.sk1er.patcher.mixins.accessors;

import net.minecraft.util.RegistrySimple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RegistrySimple.class)
public interface RegistrySimpleAccessor {
    @Accessor("registryObjects")
    Map getRegistryObjects();
}
