package club.sk1er.patcher.hooks;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class VertexLighterFlatHook {
    private static final VertexFormatElement NORMAL_4F = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.NORMAL, 4);
    private static final VertexFormat BLOCK_WITH_NORMAL = withNormalUncached(DefaultVertexFormats.BLOCK);

    public static VertexFormat withNormal(VertexFormat format) {
        if (format == DefaultVertexFormats.BLOCK) return BLOCK_WITH_NORMAL;
        return withNormalUncached(format);
    }

    private static VertexFormat withNormalUncached(VertexFormat format) {
        if (format == null || format.hasNormal()) return format;
        return new VertexFormat(format).addElement(NORMAL_4F);
    }
}
