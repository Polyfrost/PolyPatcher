package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.hooks.FaceDataHook;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ItemLayerModel;
//#if MC==10809
import net.minecraftforge.client.model.TRSRTransformation;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;
import java.util.Objects;

@Mixin(value = ItemLayerModel.class, remap = false)
public abstract class ItemLayerModelMixin_ReduceQuadCount {
    
    //#if MC==10809
    @Unique
    private static final EnumFacing[] patcher$HORIZONTALS = new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN };
    @Unique
    private static final EnumFacing[] patcher$VERTICALS = new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST };

    @Unique
    private static int patcher$size;

    @Shadow
    private static BakedQuad buildQuad(VertexFormat format,  Optional<TRSRTransformation> transform,  EnumFacing side,  int tint,  float x0,  float y0,  float z0,  float u0,  float v0,  float x1,  float y1,  float z1,  float u1,  float v1,  float x2,  float y2,  float z2,  float u2,  float v2,  float x3,  float y3,  float z3,  float u3,  float v3) {
        return null;
    }

    /**
     * @author Mixces
     * @reason Reduce quad count
     */
    @Overwrite
    public ImmutableList<BakedQuad> getQuadsForSprite(int tint,  TextureAtlasSprite sprite,  VertexFormat format,  Optional<TRSRTransformation> transform) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        int uMax = sprite.getIconWidth();
        int vMax = sprite.getIconHeight();
        FaceDataHook faceData = new FaceDataHook(uMax, vMax);
        boolean translucent = false;
        for (int f = 0; f < sprite.getFrameCount(); ++f) {
            int[] pixels = sprite.getFrameTextureData(f)[0];
            boolean[] ptv = new boolean[uMax];
            Arrays.fill(ptv, true);
            for (int v = 0; v < vMax; ++v) {
                boolean ptu = true;
                for (int u = 0; u < uMax; ++u) {
                    int alpha = patcher$getAlpha(pixels, uMax, vMax, u, v);
                    boolean t = alpha / 255.0f <= 0.1f;
                    if (!t && alpha < 255) {
                        translucent = true;
                    }
                    if (ptu && !t) {
                        faceData.set(EnumFacing.WEST, u, v);
                    }
                    if (!ptu && t) {
                        faceData.set(EnumFacing.EAST, u - 1, v);
                    }
                    if (ptv[u] && !t) {
                        faceData.set(EnumFacing.UP, u, v);
                    }
                    if (!ptv[u] && t) {
                        faceData.set(EnumFacing.DOWN, u, v - 1);
                    }
                    ptu = t;
                    ptv[u] = t;
                }
                if (!ptu) {
                    faceData.set(EnumFacing.EAST, uMax - 1, v);
                }
            }
            for (int u2 = 0; u2 < uMax; ++u2) {
                if (!ptv[u2]) {
                    faceData.set(EnumFacing.DOWN, u2, vMax - 1);
                }
            }
        }
        for (EnumFacing facing : patcher$HORIZONTALS) {
            for (int v = 0; v < vMax; ++v) {
                int uStart = 0;
                int uEnd = uMax;
                boolean building = false;
                for (int u3 = 0; u3 < uMax; ++u3) {
                    boolean face = faceData.get(facing, u3, v);
                    if (!translucent) {
                        if (face) {
                            if (!building) {
                                building = true;
                                uStart = u3;
                            }
                            uEnd = u3 + 1;
                        }
                    }
                    else if (building && !face) {
                        int off = (facing == EnumFacing.DOWN) ? 1 : 0;
                        builder.add(patcher$buildSideQuad(format, transform, facing, tint, sprite, uStart, v + off, u3 - uStart));
                        building = false;
                    }
                    else if (!building && face) {
                        building = true;
                        uStart = u3;
                    }
                }
                if (building) {
                    int off2 = (facing == EnumFacing.DOWN) ? 1 : 0;
                    builder.add(patcher$buildSideQuad(format, transform, facing, tint, sprite, uStart, v + off2, uEnd - uStart));
                }
            }
        }
        for (EnumFacing facing : patcher$VERTICALS) {
            for (int u2 = 0; u2 < uMax; ++u2) {
                int vStart = 0;
                int vEnd = vMax;
                boolean building = false;
                for (int v2 = 0; v2 < vMax; ++v2) {
                    boolean face = faceData.get(facing, u2, v2);
                    if (!translucent) {
                        if (face) {
                            if (!building) {
                                building = true;
                                vStart = v2;
                            }
                            vEnd = v2 + 1;
                        }
                    }
                    else if (building && !face) {
                        int off = (facing == EnumFacing.EAST) ? 1 : 0;
                        builder.add(patcher$buildSideQuad(format, transform, facing, tint, sprite, u2 + off, vStart, v2 - vStart));
                        building = false;
                    }
                    else if (!building && face) {
                        building = true;
                        vStart = v2;
                    }
                }
                if (building) {
                    int off2 = (facing == EnumFacing.EAST) ? 1 : 0;
                    builder.add(patcher$buildSideQuad(format, transform, facing, tint, sprite, u2 + off2, vStart, vEnd - vStart));
                }
            }
        }
        builder.add(Objects.requireNonNull(
            buildQuad(format, transform, EnumFacing.NORTH, tint,
                0.0f, 0.0f, 0.46875f, sprite.getMinU(), sprite.getMaxV(),
                0.0f, 1.0f, 0.46875f, sprite.getMinU(), sprite.getMinV(),
                1.0f, 1.0f, 0.46875f, sprite.getMaxU(), sprite.getMinV(),
                1.0f, 0.0f, 0.46875f, sprite.getMaxU(), sprite.getMaxV()
            )));
        builder.add(Objects.requireNonNull(
            buildQuad(format, transform, EnumFacing.SOUTH, tint,
                0.0f, 0.0f, 0.53125f, sprite.getMinU(), sprite.getMaxV(),
                1.0f, 0.0f, 0.53125f, sprite.getMaxU(), sprite.getMaxV(),
                1.0f, 1.0f, 0.53125f, sprite.getMaxU(), sprite.getMinV(),
                0.0f, 1.0f, 0.53125f, sprite.getMinU(), sprite.getMinV()
            )));
        return builder.build();
    }

    @Unique
    private static BakedQuad patcher$buildSideQuad(VertexFormat format,  Optional<TRSRTransformation> transform,  EnumFacing side,  int tint,  TextureAtlasSprite sprite,  int u,  int v,  int size) {
        patcher$size = size;
        return buildSideQuad(format, transform, side, tint, sprite, u, v);
    }

    @Unique
    private static int patcher$getAlpha(int[] pixels,  int uMax,  int vMax,  int u,  int v) {
        return pixels[u + (vMax - 1 - v) * uMax] >> 24 & 0xFF;
    }


    /**
     * @author Mixces
     * @reason Reduce quad count
     */
    @Overwrite
    private static BakedQuad buildSideQuad(VertexFormat format,  Optional<TRSRTransformation> transform,  EnumFacing side,  int tint,  TextureAtlasSprite sprite,  int u,  int v) {
        final float eps0 = 30e-5f;
        final float eps1 = 45e-5f;
        final float eps2 = .5f;
        final float eps3 = .5f;

        int width = sprite.getIconWidth();
        int height = sprite.getIconHeight();

        float x0 = (float) u / width;
        float y0 = (float) v / height;
        float x1 = x0, y1 = y0;
        float z0 = 7.5f / 16f - eps1, z1 = 8.5f / 16f + eps1;

        switch(side) {
            case WEST:
                z0 = 8.5f / 16f + eps1;
                z1 = 7.5f / 16f - eps1;
            case EAST:
                y1 = (float) (v + patcher$size) / height;
                break;
            case DOWN:
                z0 = 8.5f / 16f + eps1;
                z1 = 7.5f / 16f - eps1;
            case UP:
                x1 = (float) (u + patcher$size) / width;
                break;
            default:
                throw new IllegalArgumentException("can't handle z-oriented side");
        }

        float dx = side.getDirectionVec().getX() * eps3 / width;
        float dy = side.getDirectionVec().getY() * eps3 / height;

        float u0 = 16f * (x0 - dx);
        float u1 = 16f * (x1 - dx);
        float v0 = 16f * (1f - y0 - dy);
        float v1 = 16f * (1f - y1 - dy);
        switch(side) {
            case WEST:
            case EAST:
                y0 -= eps1;
                y1 += eps1;
                v0 -= eps2 / height;
                v1 += eps2 / width ;
                break;
            case DOWN:
            case UP:
                x0 -= eps1;
                x1 += eps1;
                u0 += eps2 / width;
                u1 -= eps2 / width;
                break;
            default:
                throw new IllegalArgumentException("can't handle z-oriented side");
        }
        switch(side) {
            case WEST:
                x0 += eps0;
                x1 += eps0;
                break;
            case EAST:
                x0 -= eps0;
                x1 -= eps0;
                break;
            case DOWN:
                y0 -= eps0;
                y1 -= eps0;
                break;
            case UP:
                y0 += eps0;
                y1 += eps0;
                break;
            default:
                throw new IllegalArgumentException("can't handle z-oriented side");
        }
        return buildQuad(format, transform, patcher$remap(side), tint,
            x0, y0, z0, sprite.getInterpolatedU(u0), sprite.getInterpolatedV(v0),
            x1, y1, z0, sprite.getInterpolatedU(u1), sprite.getInterpolatedV(v1),
            x1, y1, z1, sprite.getInterpolatedU(u1), sprite.getInterpolatedV(v1),
            x0, y0, z1, sprite.getInterpolatedU(u0), sprite.getInterpolatedV(v0)
        );
    }

    @Unique
    private static EnumFacing patcher$remap(EnumFacing side) {
        return !PatcherConfig.heldItemLighting || (side.getAxis() == EnumFacing.Axis.Y) ? side.getOpposite() : side;
    }
    //#endif

}
