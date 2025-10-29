package club.sk1er.patcher;

//#if FORGE
import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
//#endif

public class PolyPatcher
    //#if FORGE
    extends DummyModContainer
    //#endif
{
    //#if FORGE
    public PolyPatcher() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "polypatcher";
        meta.name = "PolyPatcher";
        meta.description = "A Forge mod full of Vanilla bug fixes, Quality of Life improvements, and performance enhancements.";
        meta.version = "@VER@";
        meta.authorList.add("Polyfrost");
        meta.authorList.add("Sk1er LLC");
        meta.credits = "prplz, 2pi, UserTeemu, DJtheRedstoner";
        meta.url = "https://modrinth.com/mod/patcher";
        meta.logoFile = "/patcher.png";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }
    //#endif
}
