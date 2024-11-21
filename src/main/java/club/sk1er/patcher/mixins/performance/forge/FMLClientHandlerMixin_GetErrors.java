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
public class FMLClientHandlerMixin_GetErrors
    //#if MC==10809
    implements FMLClientHandlerExt
    //#endif
{
    //#if MC==10809
    @Shadow(remap = false)
    private MissingModsException modsMissing;

    @Shadow(remap = false)
    private WrongMinecraftVersionException wrongMC;

    @Shadow(remap = false)
    private CustomModLoadingErrorDisplayException customError;

    @Shadow(remap = false)
    private DuplicateModsFoundException dupesFound;

    @Shadow(remap = false)
    private ModSortingException modSorting;

    @Override
    public boolean patcher$hasErrors() {
        return this.modsMissing != null || this.wrongMC != null || this.customError != null || this.dupesFound != null || this.modSorting != null;
    }
    //#endif
}
