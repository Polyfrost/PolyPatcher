package club.sk1er.patcher.mixins.performance.optifine;

import me.jellysquid.mods.lithium.common.util.math.CompactSineLUT;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.*;

@Mixin(MathHelper.class)
public class MathHelperMixin_OFFastMath {

    @Dynamic("OptiFine")
    @Shadow
    private static float[] SIN_TABLE_FAST;

    @Dynamic("OptiFine")
    @Shadow
    private static float radToIndex;

    @Dynamic("OptiFine")
    @Shadow
    public static boolean fastMath;

    /**
     * @author Wyvest
     * @reason Use a compact LUT for sine calculations
     */
    @Overwrite
    public static float sin(float f) {
        return fastMath ? SIN_TABLE_FAST[(int)(f * radToIndex) & 4095] : CompactSineLUT.sin(f);
    }

    /**
     * @author Wyvest
     * @reason Use a compact LUT for cosine calculations
     */
    @Overwrite
    public static float cos(float f) {
        return fastMath ? SIN_TABLE_FAST[(int)(f * radToIndex + 1024.0F) & 4095] : CompactSineLUT.cos(f);
    }
}
