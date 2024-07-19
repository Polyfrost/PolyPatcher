package club.sk1er.patcher.util.forge;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
//#if MC==10809
import net.minecraft.block.state.BlockState;
//#endif
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import com.google.common.base.Optional;

public class ExtendedStateImplementation
    //#if MC==10809
    extends BlockState.StateImplementation implements IExtendedBlockState
    //#endif
{
    //#if MC==10809
    private final ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties;
    private final IBlockState cleanState;

    public ExtendedStateImplementation(Block block, ImmutableMap<IProperty, Comparable> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties, @Nullable ImmutableTable<IProperty, Comparable, IBlockState> table, IBlockState clean) {
        super(block, properties, table);
        this.unlistedProperties = unlistedProperties;
        this.cleanState = clean == null ? this : clean;
    }

    @Override
    @Nonnull
    public <T extends Comparable<T>, V extends T> IBlockState withProperty(@Nonnull IProperty<T> property, @Nonnull V value) {
        IBlockState clean = super.withProperty(property, value);
        if (clean == this.cleanState) {
            return this;
        }

        if (this == this.cleanState) { // no dynamic properties present, looking up in the normal table
            return clean;
        }

        return new ExtendedStateImplementation(getBlock(), clean.getProperties(), unlistedProperties, ((BlockState.StateImplementation) clean).getPropertyValueTable(), this.cleanState);
    }

    @Override
    public <V> IExtendedBlockState withProperty(IUnlistedProperty<V> property, @Nullable V value) {
        Optional<?> oldValue = unlistedProperties.get(property);
        if (oldValue == null) {
            throw new IllegalArgumentException("Cannot set unlisted property " + property + " as it does not exist in " + getBlock().getBlockState());
        }
        if (Objects.equals(oldValue.orNull(), value)) {
            return this;
        }
        if (!property.isValid(value)) {
            throw new IllegalArgumentException("Cannot set unlisted property " + property + " to " + value + " on block " + Block.blockRegistry.getNameForObject(getBlock()) + ", it is not an allowed value");
        }
        boolean clean = true;
        ImmutableMap.Builder<IUnlistedProperty<?>, Optional<?>> builder = ImmutableMap.builder();
        for (Map.Entry<IUnlistedProperty<?>, Optional<?>> entry : unlistedProperties.entrySet()) {
            IUnlistedProperty<?> key = entry.getKey();
            Optional<?> newValue = key.equals(property) ? Optional.fromNullable(value) : entry.getValue();
            if (newValue.isPresent()) clean = false;
            builder.put(key, newValue);
        }
        if (clean) { // no dynamic properties, lookup normal state
            return (IExtendedBlockState) cleanState;
        }
        return new ExtendedStateImplementation(getBlock(), getProperties(), builder.build(), propertyValueTable, this.cleanState);
    }

    @Override
    public Collection<IUnlistedProperty<?>> getUnlistedNames() {
        return unlistedProperties.keySet();
    }

    @Override
    @Nullable
    public <V> V getValue(IUnlistedProperty<V> property) {
        Optional<?> value = unlistedProperties.get(property);
        if (value == null) {
            throw new IllegalArgumentException("Cannot get unlisted property " + property + " as it does not exist in " + getBlock().getBlockState());
        }
        return property.getType().cast(value.orNull());
    }

    public ImmutableMap<IUnlistedProperty<?>, Optional<?>> getUnlistedProperties() {
        return unlistedProperties;
    }

    @Override
    public IBlockState getClean() {
        return cleanState;
    }
    //#endif
}
