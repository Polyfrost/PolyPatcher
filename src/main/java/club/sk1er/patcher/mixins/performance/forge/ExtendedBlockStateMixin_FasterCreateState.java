package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.util.forge.ExtendedStateImplementation;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
//#if MC==10809
import net.minecraft.block.state.BlockState;
//#endif
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ExtendedBlockState.class, remap = false)
public class ExtendedBlockStateMixin_FasterCreateState
    //#if MC==10809
    extends BlockState
    //#endif
{
    //#if MC==10809
    public ExtendedBlockStateMixin_FasterCreateState(Block blockIn, IProperty... properties) {
        super(blockIn, properties);
    }

    @Inject(method = "createState", at = @At("HEAD"), cancellable = true)
    private void patcher$fasterCreateState(Block block, ImmutableMap<IProperty, Comparable> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties, CallbackInfoReturnable<StateImplementation> cir) {
        if (unlistedProperties == null || unlistedProperties.isEmpty()) {
            cir.setReturnValue(super.createState(block, properties, unlistedProperties));
        } else {
            cir.setReturnValue(new ExtendedStateImplementation(block, properties, unlistedProperties, null, null));
        }
    }
    //#endif
}
