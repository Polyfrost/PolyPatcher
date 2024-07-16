package club.sk1er.patcher.hooks;

import club.sk1er.patcher.Patcher;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.IResourceManagerReloadListener;

import java.lang.reflect.Method;
import java.util.List;

public class ResourceReloadHooks {
    private static boolean loadLanguage = false;
    private static List<IResourceManagerReloadListener> languageManager = null;
    private static Method langMethod;

    public static boolean shouldLoadLanguage() {
        return loadLanguage;
    }

    public static List<IResourceManagerReloadListener> getLanguageManager() {
        return languageManager;
    }

    public static void setLanguageManager(IResourceManagerReloadListener languageManager) {
        ResourceReloadHooks.languageManager = Lists.newArrayList(languageManager);
    }

    public static void setLoadLanguage(boolean loadLanguage) {
        ResourceReloadHooks.loadLanguage = loadLanguage;
    }

    public static void reloadOptiFineLanguage() {
        if (langMethod == null) {
            try {
                Class<?> clazz = Class.forName("net.optifine.Lang");
                langMethod = clazz.getDeclaredMethod("resourcesReloaded");
            } catch (ClassNotFoundException ignored) {
                return;
            } catch (Exception e) {
                if (Patcher.instance != null) {
                    Patcher.instance.getLogger().error("Failed to reload OptiFine language. If this is causing issues, please report this to https://polyfrost.org/discord", e);
                }
                return;
            }
        }
        try {
            langMethod.invoke(null);
        } catch (Exception e) {
            if (Patcher.instance != null) {
                Patcher.instance.getLogger().error("Failed to reload OptiFine language. If this is causing issues, please report this to https://polyfrost.org/discord", e);
            }
        }
    }
}
