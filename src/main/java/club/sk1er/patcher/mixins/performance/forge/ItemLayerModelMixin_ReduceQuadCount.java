package club.sk1er.patcher.mixins.performance.forge;

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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Objects;

@Mixin(value = ItemLayerModel.class, remap = false)
public abstract class ItemLayerModelMixin_ReduceQuadCount {
    
    //#if MC==10809
    @Unique
    private static final EnumFacing[] patcher$HORIZONTALS = new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN };;
    @Unique
    private static final EnumFacing[] patcher$VERTICALS = new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST };

    @Unique
    private static int patcher$size;

    @Shadow
    private static BakedQuad buildQuad(VertexFormat format,  Optional<TRSRTransformation> transform,  EnumFacing side,  int tint,  float x0,  float y0,  float z0,  float u0,  float v0,  float x1,  float y1,  float z1,  float u1,  float v1,  float x2,  float y2,  float z2,  float u2,  float v2,  float x3,  float y3,  float z3,  float u3,  float v3) {
        return null;
    }

    @Shadow
    private static BakedQuad buildSideQuad(VertexFormat format,  Optional<TRSRTransformation> transform,  EnumFacing side,  int tint,  TextureAtlasSprite sprite,  int u,  int v) {
        return null;
    }

    @Inject(method = "getQuadsForSprite", at = @At("HEAD"), cancellable = true)
    public void patcher$reduceQuadCount(int tint,  TextureAtlasSprite sprite,  VertexFormat format,  Optional<TRSRTransformation> transform,  CallbackInfoReturnable<ImmutableList<BakedQuad>> cir) {
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
        builder.add(Objects.requireNonNull(buildQuad(format, transform, EnumFacing.NORTH, tint, 0.0f, 0.0f, 0.46875f, sprite.getMinU(), sprite.getMaxV(), 0.0f, 1.0f, 0.46875f, sprite.getMinU(), sprite.getMinV(), 1.0f, 1.0f, 0.46875f, sprite.getMaxU(), sprite.getMinV(), 1.0f, 0.0f, 0.46875f, sprite.getMaxU(), sprite.getMaxV())));
        builder.add(Objects.requireNonNull(buildQuad(format, transform, EnumFacing.SOUTH, tint, 0.0f, 0.0f, 0.53125f, sprite.getMinU(), sprite.getMaxV(), 1.0f, 0.0f, 0.53125f, sprite.getMaxU(), sprite.getMaxV(), 1.0f, 1.0f, 0.53125f, sprite.getMaxU(), sprite.getMinV(), 0.0f, 1.0f, 0.53125f, sprite.getMinU(), sprite.getMinV())));
        cir.setReturnValue(builder.build());
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

    @Inject(method = "buildSideQuad", at = @At("HEAD"), cancellable = true)
    private static void patcher$reduceQuadCount2(VertexFormat format,  Optional<TRSRTransformation> transform,  EnumFacing side,  int tint,  TextureAtlasSprite sprite,  int u,  int v,  CallbackInfoReturnable<BakedQuad> cir) {
        int width = sprite.getIconWidth();
        int height = sprite.getIconHeight();
        float x0 = u / (float)width;
        float y0 = v / (float)height;
        float x2 = x0;
        float y2 = y0;
        float z0 = 0.46875f;
        float z2 = 0.53125f;
        if (side == EnumFacing.WEST || side == EnumFacing.EAST) {
            if (side == EnumFacing.WEST) {
                z0 = 0.53125f;
                z2 = 0.46875f;
            }
            y2 = (v + patcher$size) / (float)height;
        }
        else {
            if (side != EnumFacing.DOWN && side != EnumFacing.UP) {
                throw new IllegalArgumentException("can't handle z-oriented side");
            }
            if (side == EnumFacing.DOWN) {
                z0 = 0.53125f;
                z2 = 0.46875f;
            }
            x2 = (u + patcher$size) / (float)width;
        }
        float dx = side.getDirectionVec().getX() * 0.01f / width;
        float dy = side.getDirectionVec().getY() * 0.01f / height;
        float u2 = 16.0f * (x0 - dx);
        float u3 = 16.0f * (x2 - dx);
        float v2 = 16.0f * (1.0f - y0 - dy);
        float v3 = 16.0f * (1.0f - y2 - dy);
        cir.setReturnValue(buildQuad(format, transform, patcher$remap(side), tint, x0, y0, z0, sprite.getInterpolatedU((double)u2), sprite.getInterpolatedV((double)v2), x2, y2, z0, sprite.getInterpolatedU((double)u3), sprite.getInterpolatedV((double)v3), x2, y2, z2, sprite.getInterpolatedU((double)u3), sprite.getInterpolatedV((double)v3), x0, y0, z2, sprite.getInterpolatedU((double)u2), sprite.getInterpolatedV((double)v2)));
    }

    @Unique
    private static EnumFacing patcher$remap(EnumFacing side) {
        return (side.getAxis() == EnumFacing.Axis.Y) ? side.getOpposite() : side;
    }
    //#endif

}
