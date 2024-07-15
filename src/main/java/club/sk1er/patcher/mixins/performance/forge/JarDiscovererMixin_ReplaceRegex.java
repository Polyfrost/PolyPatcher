package club.sk1er.patcher.mixins.performance.forge;

import net.minecraftforge.fml.common.discovery.ITypeDiscoverer;
import net.minecraftforge.fml.common.discovery.JarDiscoverer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(value = JarDiscoverer.class, remap = false)
public class JarDiscovererMixin_ReplaceRegex { // todo i cba to put this in directorydiscoverer as well

    @Unique private static final Matcher patcher$dummyMatcher = ITypeDiscoverer.classFile.matcher("null");

    @Unique
    private static final String patcher$replaceRegexMethod =
        //#if MC==10809
        "discover";
        //#else
        //$$ "findClassesASM";
        //#endif

    @Unique private String patcher$fileName;

    @Redirect(method = patcher$replaceRegexMethod, at = @At(value = "INVOKE", target = "Ljava/util/regex/Pattern;matcher(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;"))
    private Matcher patcher$replaceRegex(Pattern instance, CharSequence input) {
        if (input != null) {
            patcher$fileName = input.toString();
        }
        return patcher$dummyMatcher;
    }

    @Redirect(method = patcher$replaceRegexMethod, at = @At(value = "INVOKE", target = "Ljava/util/regex/Matcher;matches()Z"))
    private boolean patcher$replaceRegex(Matcher instance) {
        return patcher$fileName != null && patcher$fileName.endsWith(".class") && !patcher$fileName.startsWith("$") && !patcher$fileName.endsWith("$");
    }
}
