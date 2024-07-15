package club.sk1er.patcher.mixins.performance;

import me.jellysquid.mods.lithium.common.util.math.CompactSineLUT;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MathHelper.class)
public class MathHelperMixin_CompactLUT {

    /**
     * @author Wyvest
     * @reason Use a compact LUT for sine calculations
     */
    @Overwrite
    public static float sin(float f) {
        return CompactSineLUT.sin(f);
    }

    /**
     * @author Wyvest
     * @reason Use a compact LUT for cosine calculations
     */
    @Overwrite
    public static float cos(float f) {
        return CompactSineLUT.cos(f);
    }
}
