package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.ducks.FMLClientHandlerExt;
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.DuplicateModsFoundException;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.WrongMinecraftVersionException;
import net.minecraftforge.fml.common.toposort.ModSortingException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FMLClientHandler.class)
public class FMLClientHandlerMixin_GetErrors implements FMLClientHandlerExt {
    @Shadow
    private MissingModsException modsMissing;

    @Shadow
    private WrongMinecraftVersionException wrongMC;

    @Shadow
    private CustomModLoadingErrorDisplayException customError;

    @Shadow
    private DuplicateModsFoundException dupesFound;

    @Shadow
    private ModSortingException modSorting;

    @Override
    public boolean patcher$hasErrors() {
        return this.modsMissing != null || this.wrongMC != null || this.customError != null || this.dupesFound != null || this.modSorting != null;
    }
}
