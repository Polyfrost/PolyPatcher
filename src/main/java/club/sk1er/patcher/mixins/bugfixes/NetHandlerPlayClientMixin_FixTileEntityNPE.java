package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin_FixTileEntityNPE {

    @Unique private boolean patcher$shouldCancelTileEntityUpdate;

    @ModifyVariable(method = "handleUpdateTileEntity", at = @At("STORE"), ordinal = 0)
    private TileEntity patcher$handleUpdateTileEntity(TileEntity tileentity) {
        if (tileentity == null) {
            patcher$shouldCancelTileEntityUpdate = true;
            return null;
        }
        return tileentity;
    }

    @Inject(method = "handleUpdateTileEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/play/server/S35PacketUpdateTileEntity;getTileEntityType()I"), cancellable = true)
    private void patcher$cancelNullTileEntityUpdate(S35PacketUpdateTileEntity packetIn, CallbackInfo ci) {
        if (patcher$shouldCancelTileEntityUpdate) {
            ci.cancel();
            patcher$shouldCancelTileEntityUpdate = false;
        }
    }
}
