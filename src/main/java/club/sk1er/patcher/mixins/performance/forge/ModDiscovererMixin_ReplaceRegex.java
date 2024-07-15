package club.sk1er.patcher.mixins.performance.forge;

import net.minecraftforge.fml.common.discovery.ModDiscoverer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(value = ModDiscoverer.class, remap = false)
public class ModDiscovererMixin_ReplaceRegex {

    //#if MC<=10809
    @Shadow
    private static Pattern zipJar;
    @Unique
    private static final Matcher patcher$dummyMatcher = zipJar.matcher("null");
    @Unique private String patcher$fileName;

    @Redirect(method = "findModDirMods(Ljava/io/File;[Ljava/io/File;)V", at = @At(value = "INVOKE", target = "Ljava/util/regex/Pattern;matcher(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;"))
    private Matcher patcher$replaceRegex(Pattern instance, CharSequence input) {
        if (input != null) {
            patcher$fileName = input.toString();
        }
        return patcher$dummyMatcher;
    }

    @Redirect(method = "findModDirMods(Ljava/io/File;[Ljava/io/File;)V", at = @At(value = "INVOKE", target = "Ljava/util/regex/Matcher;matches()Z"))
    private boolean patcher$replaceRegex(Matcher instance) {
        return patcher$fileName != null && (patcher$fileName.endsWith(".jar") || patcher$fileName.endsWith(".zip"));
    }

    @Redirect(method = "findModDirMods(Ljava/io/File;[Ljava/io/File;)V", at = @At(value = "INVOKE", target = "Ljava/util/regex/Matcher;group(I)Ljava/lang/String;"))
    private String patcher$replaceRegex(Matcher instance, int group) {
        return patcher$fileName; // we could cut out the file extension but it's not necessary cause this is just for logging
    }

    //#endif
}
