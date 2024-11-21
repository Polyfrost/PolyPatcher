package club.sk1er.patcher.util.forge;

import club.sk1er.patcher.config.PatcherConfig;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModContainerFactory;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ModCandidate;
import net.minecraftforge.fml.common.discovery.asm.ASMModParser;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.polyfrost.oneconfig.api.ui.v1.Notifications;
import org.polyfrost.polyui.unit.Units;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class EntrypointCaching {

    public static EntrypointCaching INSTANCE = new EntrypointCaching();

    public final Logger logger = LogManager.getLogger("Patcher Entrypoint Cache");
    private final Type mapType = new TypeToken<List<Map<String, List<String>>>>() {}.getType();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File cacheFile = new File("patcher/entrypoint_cache.json");

    private List<Map<String, List<String>>> readMap;
    private List<Map<String, List<String>>> usedMap = ImmutableList.of(new HashMap<>(), new HashMap<>());
    private Map<File, String> hashCache = new HashMap<>();

    private EntrypointCaching() {
        if (!PatcherConfig.cacheEntrypoints) return;
        try {
            if (cacheFile.exists()) {
                String cacheText = FileUtils.readFileToString(cacheFile);
                readMap = gson.fromJson(cacheText, mapType);
                return;
            }
        } catch (Exception e) {
            logger.error("Failed to read entrypoint cache", e);
        }
        readMap = ImmutableList.of(new HashMap<>(), new HashMap<>());
    }

    @SuppressWarnings("unused")
    public List<ModContainer> discoverCachedEntrypoints(ModCandidate candidate, ASMDataTable table, JarFile file, MetadataCollection mc) {
        if (!PatcherConfig.cacheEntrypoints || readMap == null) return null;
        File modFile = candidate.getModContainer();

        String hash = getHash(modFile);
        if (hash == null) return null;

        List<String> entryClasses = readMap.get(0).get(hash);
        List<String> modClasses = readMap.get(1).get(hash);
        if (entryClasses == null && modClasses == null) return null;

        List<ModContainer> foundMods = new ArrayList<>();
        List<String> validEntries = null;
        if (entryClasses != null && !entryClasses.isEmpty()) {
            validEntries = new ArrayList<>(entryClasses.size());
            for (String modClass : entryClasses) {
                iterateThroughClass(true, candidate, table, file, mc, modFile, foundMods, validEntries, modClass);
            }
        }

        List<String> validClasses = null;
        if (modClasses != null && !modClasses.isEmpty()) {
            validClasses = new ArrayList<>(modClasses.size());
            for (String modClass : modClasses) {
                iterateThroughClass(false, candidate, table, file, mc, modFile, foundMods, validClasses, modClass);
            }
        }

        logger.info("Found cached entrypoints for " + modFile);

        try {
            file.close();
        } catch (Exception e) {
            logger.error("Error closing mod jar " + modFile, e);
        }

        usedMap.get(0).put(hash, (validEntries));
        usedMap.get(1).put(hash, (validClasses));

        return foundMods;
    }

    private void iterateThroughClass(boolean entry, ModCandidate candidate, ASMDataTable table, JarFile file, MetadataCollection mc, File modFile, List<ModContainer> foundMods, List<String> validMods, String modClass) {
        candidate.addClassEntry(modClass);

        if (entry) {
            try (InputStream is = file.getInputStream(new JarEntry(modClass))) {
                ASMModParser modParser = new ASMModParser(is);
                modParser.validate();
                modParser.sendToTable(table, candidate);
                ModContainer container = ModContainerFactory.instance().build(modParser, modFile, candidate);
                if (container != null) {
                    table.addContainer(container);
                    foundMods.add(container);
                    validMods.add(modClass);
                    container.bindMetadata(mc);
                    //#if MC>10809
                    //$$ container.setClassVersion(modParser.getClassVersion());
                    //#endif
                }
            } catch (Exception e) {
                logger.error("Error parsing mod class " + modClass + " from jar " + modFile, e);
            }
        } else {
            validMods.add(modClass);
        }
    }

    @SuppressWarnings("unused")
    public void putCachedEntrypoints(ModCandidate candidate, ZipEntry ze) {
        if (!PatcherConfig.cacheEntrypoints || readMap == null) return;
        File modFile = candidate.getModContainer();
        String modClass = ze.getName();

        String hash = hashCache.computeIfAbsent(modFile, this::getHash);
        List<String> modClasses = usedMap.get(0).computeIfAbsent(hash, h -> new ArrayList<>());
        if (!modClasses.contains(modClass)) {
            modClasses.add(modClass);
        }

        logger.info("Added entrypoint {} for mod jar {}", modClass, modFile);
    }

    @SuppressWarnings("unused")
    public void putCachedClassEntries(ModCandidate candidate, ZipEntry ze) {
        if (!PatcherConfig.cacheEntrypoints || readMap == null) return;

        File modFile = candidate.getModContainer();
        String modClass = ze.getName();

        String hash = hashCache.computeIfAbsent(modFile, this::getHash);
        List<String> modClasses = usedMap.get(1).computeIfAbsent(hash, h -> new ArrayList<>());
        if (!modClasses.contains(modClass)) {
            modClasses.add(modClass);
        }
    }

    public void onInit() {
        if (!PatcherConfig.cacheEntrypoints || readMap == null) return;
        readMap = null;

        File patcherDir = new File("patcher");
        if (!patcherDir.exists() && !patcherDir.mkdir()) {
            logger.error("Failed to create patcher directory!");
        }
        try {
            if (!cacheFile.exists() && !cacheFile.createNewFile()) {
                logger.error("Failed to create entrypoint cache");
            }
        } catch (Exception e) {
            logger.error("Failed to create entrypoint cache", e);
        }

        String jsonString = gson.toJson(usedMap, mapType);
        try {
            FileUtils.write(cacheFile, jsonString);
        } catch (Exception e) {
            logger.error("Failed to write entrypoint cache", e);
        }

        usedMap = null;
        hashCache = null;
    }

    private String getHash(File modFile) {
        try (FileInputStream fis = new FileInputStream(modFile)) {
            byte[] bytes = new byte[2048];
            int read = fis.read(bytes);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes, 0, read);
            long length = modFile.length();
            md.update(new byte[]{
                (byte) ((length >> 56) & 0xff),
                (byte) ((length >> 48) & 0xff),
                (byte) ((length >> 40) & 0xff),
                (byte) ((length >> 32) & 0xff),
                (byte) ((length >> 24) & 0xff),
                (byte) ((length >> 16) & 0xff),
                (byte) ((length >> 8) & 0xff),
                (byte) (length & 0xff)
            });
            return Hex.encodeHexString(md.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            logger.error("Error hashing mod {}", modFile, e);
        }
        return null;
    }

    public void resetCache() {
        if (cacheFile.exists()) {
            if (cacheFile.delete()) {
                Notifications.enqueue(Notifications.Type.Info, "PolyPatcher", "Deleted entrypoint cache", Units.seconds(5));
                logger.info("Deleted entrypoint cache");
            } else {
                Notifications.enqueue(Notifications.Type.Error, "PolyPatcher", "Failed to delete entrypoint cache!", Units.seconds(5));
                logger.error("Failed to delete entrypoint cache");
            }
        }
    }
}
