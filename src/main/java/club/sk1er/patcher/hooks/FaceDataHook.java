package club.sk1er.patcher.hooks;

import net.minecraft.util.EnumFacing;

import java.util.BitSet;
import java.util.EnumMap;

public class FaceDataHook {

    private final EnumMap<EnumFacing, BitSet> data;
    private final int vMax;

    public FaceDataHook(final int uMax, final int vMax) {
        this.data = new EnumMap<>(EnumFacing.class);
        this.vMax = vMax;
        int size = uMax * vMax;
        BitSet initialData = new BitSet(size);
        this.data.put(EnumFacing.WEST, initialData);
        this.data.put(EnumFacing.EAST, new BitSet(size));
        this.data.put(EnumFacing.UP, new BitSet(size));
        this.data.put(EnumFacing.DOWN, new BitSet(size));
    }

    public void set(final EnumFacing facing, final int u, final int v) {
        BitSet dataSet = this.data.get(facing);
        dataSet.set(getIndex(u, v));
    }

    public boolean get(final EnumFacing facing, final int u, final int v) {
        BitSet dataSet = this.data.get(facing);
        return dataSet.get(getIndex(u, v));
    }

    private int getIndex(final int u, final int v) {
        return v * this.vMax + u;
    }

}
