package club.sk1er.patcher.mixins.plugin;

import club.sk1er.patcher.tweaker.ClassTransformer;
import club.sk1er.patcher.tweaker.transform.PatcherTransformer;
import com.google.common.collect.ArrayListMultimap;
import kotlin.text.StringsKt;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class PatcherMixinPlugin implements IMixinConfigPlugin {
    private static final String LABYMOD_CLASS = "net/labymod/main/LabyMod.class";
    private static final String SMOOTHFONT_CLASS = "bre/smoothfont/mod_SmoothFont.class";
    private static final String OF_CONFIG_CLASS = "Config.class";

    private static final ArrayListMultimap<String, String> CONFLICTING_CLASSES = ArrayListMultimap.create();
    private static boolean isEarsMod = false;

    private final ClassTransformer transformer;

    static {
        MixinEnvironment.getDefaultEnvironment().addTransformerExclusion("com.unascribed.ears.asm.PlatformTransformerAdapter");
        CONFLICTING_CLASSES.put("GuiContainerMixin_MouseBindFixThatLabyBreaks", LABYMOD_CLASS);
        CONFLICTING_CLASSES.put("FontRendererMixin_Optimization", SMOOTHFONT_CLASS);
        CONFLICTING_CLASSES.put("MathHelperMixin_CompactLUT", OF_CONFIG_CLASS);
    }

    public PatcherMixinPlugin() {
        this.transformer = new ClassTransformer();
    }

    @Override
    public void onLoad(String mixinPackage) {
        try {
            Class.forName("com.unascribed.ears.Ears", false, getClass().getClassLoader());
            isEarsMod = true;
        } catch (ClassNotFoundException ignored) {
            isEarsMod = false;
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.endsWith("_EarsMod")) {
            return isEarsMod;
        }
        String mixinPackage = StringsKt.substringBeforeLast(mixinClassName, '.', mixinClassName);
        if (mixinPackage.endsWith("optifine") && "NONE".equals(ClassTransformer.optifineVersion)) {
            // OptiFine isn't present, let's not apply this
            return false;
        }
        for (String conflictingClass : CONFLICTING_CLASSES.get(StringsKt.substringAfterLast(mixinClassName, '.', mixinClassName))) {
            if (this.getClass().getClassLoader().getResource(conflictingClass) != null) {
                // Conflicting class is present, let's not apply this
                return false;
            }
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        Collection<PatcherTransformer> transformers = this.transformer.transformerMap.get(targetClassName);
        if (transformers != null) {
            for (PatcherTransformer patcherTransformer : transformers) {
                patcherTransformer.transform(targetClass, targetClassName);
            }
        }
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
