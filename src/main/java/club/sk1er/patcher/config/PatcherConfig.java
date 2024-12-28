package club.sk1er.patcher.config;

//#if FORGE
import net.minecraftforge.common.ForgeVersion;
//#endif

import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.Property;
import org.polyfrost.oneconfig.api.config.v1.annotations.Number;
import org.polyfrost.oneconfig.api.config.v1.annotations.*;
import club.sk1er.patcher.Patcher;
import club.sk1er.patcher.tweaker.ClassTransformer;
import net.minecraft.client.Minecraft;
import java.util.Arrays;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class PatcherConfig extends Config {

    // BUG FIXES

    @Switch(
        title = "Keep Shaders on Perspective Change",
        description = "Resolve Vanilla shaders being cleared when changing perspective.",
        category = "Bug Fixes", subcategory = "General"
    )
    public static boolean keepShadersOnPerspectiveChange = true;

    @Switch(
        title = "Parallax Fix",
        description = "Resolve the camera being too far back, seemingly making your eyes be in the back of your head.",
        category = "Bug Fixes", subcategory = "General"
    )
    public static boolean parallaxFix;

    //@Info(
    //    text = "Culling Fix can negatively impact performance.",
    //    category = "Bug Fixes", subcategory = "General",
    //    type = InfoType.ERROR
    //)
    //private static String cullingFixInfo = "";

    @Switch(
        title = "Culling Fix",
        description = "Resolve false negatives in frustum culling, creating invisible chunks in some cases.",
        category = "Bug Fixes", subcategory = "General"
    )
    public static boolean cullingFix;

    @Switch(
        title = "Resource Exploit Fix",
        description = "Resolve an exploit in 1.8 allowing servers to look through directories.",
        category = "Bug Fixes", subcategory = "Security"
    )
    public static boolean resourceExploitFix = true;

    @Switch(
        title = "Layers In Tab",
        description = "Resolve players sometimes not having a hat layer in Tab.",
        category = "Bug Fixes", subcategory = "General"
    )
    public static boolean layersInTab = true;

    @Switch(
        title = "Player Void Rendering",
        description = "Resolve the black box around the player while in the void.",
        category = "Bug Fixes", subcategory = "Rendering"
    )
    public static boolean playerVoidRendering = true;

    //@Info(
    //    text = "OptiFine Custom Sky Fix also changes the rendering of the normal sky. Some people may prefer the original rendering.",
    //    category = "Bug Fixes", subcategory = "Rendering",
    //    type = InfoType.WARNING
    //)
    //private static String customSkyFixInfo = "";

    @Switch(
        title = "OptiFine Custom Sky Fix",
        description = "Resolve OptiFine creating a \"black box\" effect at the bottom of the sky when using custom skies.",
        category = "Bug Fixes", subcategory = "Rendering"
    )
    public static boolean customSkyFix = true;

    //@Info(
    //    text = "Alex Arm Position requires a restart once toggled.",
    //    category = "Bug Fixes", subcategory = "Rendering",
    //    type = InfoType.WARNING
    //)
    //private static String fixedAlexArmsInfo = "";

    @Switch(
        title = "Alex Arm Position",
        description = "Resolve Alex-model arms being shifted down further than Steve-model arms.",
        category = "Bug Fixes", subcategory = "Rendering"
    )
    public static boolean fixedAlexArms = true;

    @Switch(
        title = "Add Background to Book GUI",
        description = "Adds the dark background to the book GUI like all other containers/menus.",
        category = "Bug Fixes", subcategory = "Rendering"
    )
    public static boolean bookBackground = false;

    @Dropdown(
        title = "Keyboard Layout",
        description = "The layout of your keyboard, used to fix input bugs accordingly.",
        category = "Bug Fixes", subcategory = "Linux",
        options = {"QWERTY", "BE AZERTY", "FR AZERTY", "DE QWERTZ"}
    )
    public static int keyboardLayout = 0;

    @Switch(
        title = "Vanilla Held Item Lighting",
        description = "Amends a Forge bug causing item sides to have incorrect lighting compared to Vanilla 1.8.",
        category = "Bug Fixes", subcategory = "Forge"
    )
    public static boolean heldItemLighting = true;

    @Switch(
        title = "Vanilla Glass Panes",
        description = "Reverts a Forge change causing Glass Panes and Iron Bars to connect where they shouldn't.",
        category = "Bug Fixes", subcategory = "Forge"
    )
    public static boolean vanillaGlassPanes;

    // MISCELLANEOUS

    //@Info(
    //    text = "Better Keybind Handling does not work on macOS due to LWJGL issues.",
    //    category = "Miscellaneous", subcategory = "General",
    //    type = InfoType.ERROR,
    //    size = 2
    //)
    //private static String betterKeybindHandlingInfo = "";

    @Switch(
        title = "Better Keybind Handling",
        description = "Make keys re-register when closing a GUI, like in 1.12+.",
        category = "Miscellaneous", subcategory = "General"
    )
    public static boolean newKeybindHandling = true;

    @Switch(
        title = "Separate Sound & Texture Reloading",
        description = "Separate reloading resources into reloading sounds (F3+S) and reloading textures (F3+T).",
        category = "Miscellaneous", subcategory = "General"
    )
    public static boolean separateResourceLoading;

    //@Info(
    //    text = "Fullbright can positively impact performance. It may conflict with minimaps mods.",
    //    category = "Miscellaneous", subcategory = "Rendering",
    //    type = InfoType.WARNING,
    //    size = 2
    //)
    //private static String fullbrightInfo = "";

    @Switch(
        title = "Fullbright",
        description = "Remove lighting updates, increasing visibility.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    public static boolean fullbright = true;

    @Switch(
        title = "Smart Fullbright",
        description = "Automatically disable the Fullbright effect when using OptiFine shaders.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    public static boolean smartFullbright = true;

    @Switch(
        title = "Disable Night Vision",
        description = "Completely disable the effects of night vision.",
        category = "Miscellaneous", subcategory = "Overlays"
    )
    public static boolean disableNightVision = false;

    @Switch(
        title = "Cleaner Night Vision",
        description = "Make the night vision effect fade out instead of a flashing effect.",
        category = "Miscellaneous", subcategory = "Overlays"
    )
    public static boolean cleanerNightVision = false;

    @Switch(
        title = "Disable Achievements",
        description = "Remove achievement notifications.",
        category = "Miscellaneous", subcategory = "Overlays"
    )
    public static boolean disableAchievements;

    @Slider(
        title = "Fire Overlay Height",
        description = "Change the height of the fire overlay.",
        category = "Miscellaneous", subcategory = "Overlays",
        min = -0.5F, max = 1.5F
    )
    public static float fireOverlayHeight;

    @Slider(
        title = "Fire Overlay Opacity (%)",
        description = "Change the opacity of the fire overlay.",
        category = "Miscellaneous", subcategory = "Overlays",
        min = 0, max = 100
    )
    public static int fireOverlayOpacityI = 100;

    @Switch(
        title = "Hide Fire Overlay with Fire Resistance",
        description = "Hide the fire overlay when you have fire resistance active.\n" +
            "The overlay will blink 5 seconds before your fire resistance is about to run out.",
        category = "Miscellaneous", subcategory = "Overlays"
    )
    public static boolean hideFireOverlayWithFireResistance;

    @Slider(
        title = "Pumpkin Overlay Opacity (%)",
        description = "Change the opacity of the pumpkin overlay.",
        category = "Miscellaneous", subcategory = "Overlays",
        min = 0, max = 100
    )
    public static int pumpkinOverlayOpacity = 100;

    @Switch(
        title = "Automatically Scale Title",
        description = "Automatically scale titles if the title goes over the screen.",
        category = "Miscellaneous", subcategory = "Titles"
    )
    public static boolean autoTitleScale;

    // fog



    // fov

    @Switch(
        title = "Remove Water FOV",
        description = "Remove FOV change when underwater.",
        category = "Miscellaneous", subcategory = "Field of View"
    )
    public static boolean removeWaterFov = true;

    @Switch(
        title = "FOV Modifier",
        description = "Allow for modifying FOV change states.",
        category = "Miscellaneous", subcategory = "Field of View"
    )
    public static boolean allowFovModifying;

    @Slider(
        title = "Sprinting FOV",
        description = "Modify your FOV when sprinting.",
        category = "Miscellaneous", subcategory = "Field of View",
        min = -5F, max = 5F
    )
    public static float sprintingFovModifierFloat = 1;

    @Slider(
        title = "Flying FOV",
        description = "Modify your FOV when flying.",
        category = "Miscellaneous", subcategory = "Field of View",
        min = -5F, max = 5F
    )
    public static float flyingFovModifierFloat = 1;

    @Slider(
        title = "Bow FOV",
        description = "Modify your FOV when pulling back a bow.",
        category = "Miscellaneous", subcategory = "Field of View",
        min = -5, max = 5
    )
    public static float bowFovModifierFloat = 1;

    @Slider(
        title = "Speed FOV",
        description = "Modify your FOV when having the speed effect.",
        category = "Miscellaneous", subcategory = "Field of View",
        min = -5, max = 5
    )
    public static float speedFovModifierFloat = 1;

    @Slider(
        title = "Slowness FOV",
        description = "Modify your FOV when having the slowness effect.",
        category = "Miscellaneous", subcategory = "Field of View",
        min = -5, max = 5
    )
    public static float slownessFovModifierFloat = 1;

    @Switch(
        title = "Disable Hotbar Scrolling",
        description = "Remove the ability to scroll through your hotbar.",
        category = "Miscellaneous", subcategory = "General"
    )
    public static boolean disableHotbarScrolling;

    @Switch(
        title = "Invert Hotbar Scrolling",
        description = "Change the direction of scrolling in your hotbar.",
        category = "Miscellaneous", subcategory = "General"
    )
    public static boolean invertHotbarScrolling;

    @Switch(
        title = "Prevent Overflow Hotbar Scrolling",
        description = "Prevent from directly scrolling between the first and last hotbar slot.",
        category = "Miscellaneous", subcategory = "General"
    )
    public static boolean preventOverflowHotbarScrolling;

    @Slider(
        title = "Unfocused Sounds",
        description = "Change the volume of sounds when you're not tabbed into the window.",
        category = "Miscellaneous", subcategory = "General",
        min = 0F, max = 1.0F
    )
    public static float unfocusedSounds = 1.0F;

    @Switch(
        title = "Unfocused FPS",
        description = "Toggle changing your FPS to whatever Unfocused FPS is set to when not tabbed into the window.",
        category = "Miscellaneous", subcategory = "General"
    )
    public static boolean unfocusedFPS;

    @Slider(
        title = "Unfocused FPS Amount",
        description = "Change the maximum FPS when you're not tabbed into the window, saving resources.",
        category = "Miscellaneous", subcategory = "General",
        min = 15, max = 240
    )
    public static int unfocusedFPSAmount = 60;

    @Number(
        title = "Custom FPS Limit",
        description = "Change the maximum FPS to a value that Minecraft doesn't normally allow for. Setting this to 0 will go back to the value set in Minecraft.",
        category = "Miscellaneous", subcategory = "General",
        min = 0,
        max = Integer.MAX_VALUE
    )
    public static int customFpsLimit = 0;

    @Switch(
        title = "Remove Ground Foliage",
        description = "Stop plants/flowers from rendering.",
        category = "Miscellaneous", subcategory = "Blocks"
    )
    public static boolean removeGroundFoliage;

    @Switch(
        title = "Clean Projectiles",
        description = "Show projectiles 2 ticks after they're shot to stop them from obstructing your view.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    public static boolean cleanProjectiles;

    @Slider(
        title = "Ridden Horse Opacity (%)",
        description = "Change the opacity of the horse you're currently riding for visibility.",
        category = "Miscellaneous", subcategory = "Rendering",
        min = 0F, max = 100
    )
    public static int riddenHorseOpacityI = 100;

    @Slider(
        title = "Distortion Effects (%)",
        description = "Changes the distortion effects (e.g. Nausea and nether portal distortion).",
        category = "Miscellaneous", subcategory = "Rendering",
        min = 0, max = 100
    )
    public static int distortionEffect = 100;

    @Slider(
        title = "Water Fog Density (%)",
        description = "Changes the fog density in water to improve visibility.",
        category = "Miscellaneous", subcategory = "Fog",
        min = 0, max = 100
    )
    public static int waterDensity = 100;

    @Switch(
        title = "Hide Aura on Invisible Withers",
        description = "Don't render the aura around a wither when it is invisible.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    public static boolean hideAuraOnInvisibleWither;

    @Switch(
        title = "Zoom Adjustment",
        description = "Scroll when using OptiFine's zoom to adjust the zoom level.",
        category = "Miscellaneous", subcategory = "OptiFine"
    )
    public static boolean scrollToZoom = true;

    @Switch(
        title = "Remove Smooth Camera While Zoomed",
        description = "Remove the smooth camera effect when using zoom.",
        category = "Miscellaneous", subcategory = "OptiFine"
    )
    public static boolean normalZoomSensitivity;

    @Switch(
        title = "Render Hand While Zoomed",
        description = "Keep your hand on screen when you zoom in.",
        category = "Miscellaneous", subcategory = "OptiFine"
    )
    public static boolean renderHandWhenZoomed;

    @Slider(
        title = "Zoom Sensitivity",
        description = "Use a custom mouse sensitivity value when zoomed in.",
        category = "Miscellaneous", subcategory = "OptiFine",
        min = 0F, max = 1F
    )
    public static float customZoomSensitivity = 1.0F;

    @Switch(
        title = "Dynamic Zoom Sensitivity",
        description = "Reduce your mouse sensitivity the more you zoom in.",
        category = "Miscellaneous", subcategory = "OptiFine"
    )
    public static boolean dynamicZoomSensitivity;

    @Switch(
        title = "Smooth Zoom Animation",
        description = "Add a smooth animation when you zoom in and out.",
        category = "Miscellaneous", subcategory = "OptiFine"
    )
    public static boolean smoothZoomAnimation;

    @Switch(
        title = "Smooth Scroll-to-Zoom Animation",
        description = "Add a smooth animation when you scroll in and out while zoomed.",
        category = "Miscellaneous", subcategory = "OptiFine"
    )
    public static boolean smoothZoomAnimationWhenScrolling;

    @Dropdown(
        title = "Smooth Zoom Function",
        description = "Change the smoothing function used in the smooth zooming animation.",
        category = "Miscellaneous", subcategory = "OptiFine",
        options = {"In Out Quad", "In Out Circular", "Out Quint"}
    )
    public static int smoothZoomAlgorithm = 0;

    @Switch(
        title = "Toggle to Zoom",
        description = "Make OptiFine's zoom key a toggle instead of requiring you to hold it.",
        category = "Miscellaneous", subcategory = "OptiFine"
    )
    public static boolean toggleToZoom;

    @Switch(
        title = "Simplify FPS Counter",
        description = "Remove the extra FPS counter added by OptiFine.",
        category = "Miscellaneous", subcategory = "OptiFine"
    )
    public static boolean normalFpsCounter = true;

    @Switch(
        title = "Use Vanilla Metrics Renderer",
        description = "Replace OptiFine's ALT+F3 metrics renderer with the Vanilla renderer.",
        category = "Miscellaneous", subcategory = "OptiFine"
    )
    public static boolean useVanillaMetricsRenderer = true;

    @Switch(
        title = "Numerical Enchantments",
        description = "Use readable numbers instead of Roman numerals on enchants.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    public static boolean numericalEnchants;

    @Switch(
        title = "Translate Unknown Roman Numerals",
        description = "Generate Roman numeral from enchantment and potion levels instead of using language file.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    public static boolean betterRomanNumerals = true;

    @Switch(
        title = "Windowed Fullscreen",
        description = "Implement Windowed Fullscreen in Minecraft, allowing you to drag your mouse outside the window.",
        category = "Miscellaneous", subcategory = "Window"
    )
    public static boolean windowedFullscreen;

    @Switch(
        title = "Instant Fullscreen",
        description = "Instant switching between fullscreen and non-fullscreen modes.",
        category = "Miscellaneous", subcategory = "Window"
    )
    public static boolean instantFullscreen;

    @Switch(
        title = "Remove Water Overlay",
        description = "Remove the water texture overlay when underwater.",
        category = "Miscellaneous", subcategory = "Overlays"
    )
    public static boolean removeWaterOverlay;

    @Switch(
        title = "Disable Lightning Bolts",
        description = "Stop lightning bolts from appearing.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    public static boolean disableLightningBolts;

    //@Info(
    //    text = "Files deleted by Log Optimizer are not recoverable.",
    //    category = "Miscellaneous", subcategory = "General",
    //    type = InfoType.WARNING
    //)
    //private static boolean logOptimizerInfo = true;

    @Switch(
        title = "Log Optimizer",
        description = "Delete all files in the logs folder, as these can usually take up a lot of space.",
        category = "Miscellaneous", subcategory = "General"
    )
    public static boolean logOptimizer;

    @Slider(
        title = "Log Optimizer Amount",
        description = "Choose how many days old a file must be before being deleted.",
        category = "Miscellaneous", subcategory = "General",
        min = 1, max = 90
    )
    public static int logOptimizerLength = 30;

    //@Info(
    //    text = "1.12 Farm Selection Boxes only works on Hypixel & Singleplayer.",
    //    category = "Miscellaneous", subcategory = "Blocks",
    //    type = InfoType.WARNING,
    //    size = 2
    //)
    //private static boolean farmSelectionBoxesInfo = true;

    @Switch(
        title = "1.12 Farm Selection Boxes",
        description = "Replaces the selection box for crops with the 1.12 variant.",
        category = "Miscellaneous", subcategory = "Blocks"
    )
    public static boolean futureHitBoxes = true;

    //@Info(
    //    text = "Exclude Cacti from 1.12 Boxes requires a restart once toggled.",
    //    category = "Miscellaneous", subcategory = "Blocks",
    //    type = InfoType.WARNING,
    //    size = 2
    //)
    //private static boolean cactusHitboxExclusionInfo = true;

    @Switch(
        title = "Exclude Cacti from 1.12 Boxes",
        description = "Exclude cacti from the 1.12 selection box changes, as it would actually shrink rather than increase in size.",
        category = "Miscellaneous", subcategory = "Blocks"
    )
    public static boolean cactusHitboxExclusion = true;

    @Switch(
        title = "Alternate Text Shadow",
        description = "Change the text-shadow to only move down rather than move to the side.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    public static boolean alternateTextShadow;

    //@Info(
    //    text = "Disable Text Shadow can positively impact performance.",
    //    category = "Miscellaneous", subcategory = "Rendering",
    //    type = InfoType.INFO
    //)
    //private static boolean disableTextShadowInfo = true;

    @Switch(
        title = "Disable Text Shadow",
        description = "Remove shadows from text.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    public static boolean disableShadowedText;

    @Switch(
        title = "Left Hand in First Person",
        description = "Render the first-person hand on the left of the screen.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    public static boolean leftHandInFirstPerson;

    @Switch(
        title = "Better Camera",
        description = "Stop tall grass, plants, reeds, etc. from affecting your FOV as done in 1.14+.",
        category = "Miscellaneous", subcategory = "General"
    )
    public static boolean betterCamera = true;

    @Switch(
        title = "Better F1",
        description = "Hide nametags when in F1 mode.",
        category = "Miscellaneous", subcategory = "General"
    )
    public static boolean betterHideGui;

    @Switch(
        title = "Remove Screen Bobbing",
        description = "While using View Bobbing, only remove the view aspect but have the hand still bounce around.",
        category = "Miscellaneous", subcategory = "General"
    )
    public static boolean removeViewBobbing;

    @Switch(
        title = "Remove Map Bobbing",
        description = "While using View Bobbing, remove the hand bobbing when holding a map.",
        category = "Miscellaneous", subcategory = "General"
    )
    public static boolean mapBobbing;

    @Switch(
        title = "Static Items",
        description = "Stop items from bobbing up and down when dropped on the ground.",
        category = "Miscellaneous", subcategory = "General"
    )
    public static boolean staticItems;

    @Button(
        title = "Modify Every Sound",
        text = "Modify",
        description = "Open a separate GUI allowing you to mute or amplify individual sounds.",
        category = "Miscellaneous", subcategory = "General"
    )
    private void modifyEverySound() {
        //todo
        //Patcher.instance.getPatcherSoundConfig().openGui();
    }

    @Switch(
        title = "Natural Capes",
        description = "Changes some physics in capes to fix rotation bugs and look more natural.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    public static boolean naturalCapes;

    @Switch(
        title = "Smooth Scrolling",
        description = "Smoothly scrolls through vanilla Minecraft GUIs.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    public static boolean smoothScrolling;

    // PERFORMANCE

    @Switch(
        title = "Optimized World Swapping",
        description = "Remove unnecessary garbage collection & screen displaying to make world swapping feel nearly instant.",
        category = "Performance", subcategory = "World"
    )
    public static boolean optimizedWorldSwapping = true;

    @Switch(
        title = "Limit Chunk Updates",
        description = "Limit the number of chunk updates that happen a second.",
        category = "Performance", subcategory = "World"
    )
    public static boolean limitChunks;

    @Slider(
        title = "Chunk Update Limit",
        description = "Specify the number of updates that can happen a second.",
        category = "Performance", subcategory = "World",
        min = 5, max = 250
    )
    public static int chunkUpdateLimit = 50;

    @Switch(
        title = "Downscale Pack Images",
        description = "Change all pack icons to 64x64 to reduce memory usage.",
        category = "Performance", subcategory = "Resources"
    )
    public static boolean downscalePackImages = true;

    @Switch(
        title = "Low Animation Tick",
        description = "Lowers the number of animations that happen a second from 1000 to 500.",
        category = "Performance", subcategory = "World"
    )
    public static boolean lowAnimationTick = true;

    @Switch(
        title = "Batch Model Rendering",
        description = "Render models in a single draw call.",
        category = "Performance", subcategory = "World"
    )
    public static boolean batchModelRendering = true;

    @Switch(
        title = "Optimized Font Renderer",
        description = "Use modern rendering techniques to improve the font renderer performance.",
        category = "Performance", subcategory = "Text Rendering"
    )
    public static boolean optimizedFontRenderer = true;

    @Switch(
        title = "Cache Font Data",
        description = "Cache font data, allowing for it to be reused multiple times before needing recalculation.",
        category = "Performance", subcategory = "Text Rendering"
    )
    public static boolean cacheFontData = true;

    //@Info(
    //    text = "Armor stands are commonly used for NPC nametags. Enabling Disable Armorstands will stop those from rendering as well.",
    //    category = "Performance", subcategory = "Entity Rendering",
    //    type = InfoType.ERROR,
    //    size = 2
    //)
    //private static boolean armorStandInfo = true;

    @Switch(
        title = "Disable Armorstands",
        description = "Stop armor stands from rendering.",
        category = "Performance", subcategory = "Entity Rendering"
    )
    public static boolean disableArmorstands;

    @Switch(
        title = "Disable Semitransparent Players",
        description = "Stop semitransparent players from rendering.",
        category = "Performance", subcategory = "Entity Rendering"
    )
    public static boolean disableSemitransparentEntities;

    @Switch(
        title = "Disable Enchantment Books",
        description = "Stop enchantment table books from rendering.",
        category = "Performance", subcategory = "Entity Rendering"
    )
    public static boolean disableEnchantmentBooks;

    @Switch(
        title = "Disable Item Frames",
        description = "Stop item frames from rendering.",
        category = "Performance", subcategory = "Entity Rendering"
    )
    public static boolean disableItemFrames;

    @Switch(
        title = "Disable Mapped Item Frames",
        description = "Stop item frames only with maps as their item from rendering.",
        category = "Performance", subcategory = "Entity Rendering"
    )
    public static boolean disableMappedItemFrames;

    @Switch(
        title = "Disable Grounded Arrows",
        description = "Stop arrows that are in the ground from rendering.",
        category = "Performance", subcategory = "Entity Rendering"
    )
    public static boolean disableGroundedArrows;

    @Switch(
        title = "Disable Attached Arrows",
        description = "Stop arrows that are attached to a player from rendering.",
        category = "Performance", subcategory = "Entity Rendering"
    )
    public static boolean disableAttachedArrows;

    @Switch(
        title = "Disable Moving Arrows",
        description = "Stop arrows that are airborne from rendering.",
        category = "Performance", subcategory = "Entity Rendering"
    )
    public static boolean disableMovingArrows;

    @Switch(
        title = "Disable Skulls",
        description = "Stop skulls from rendering.",
        category = "Performance", subcategory = "Entity Rendering"
    )
    public static boolean disableSkulls;

    @Switch(
        title = "Disable Falling Blocks",
        description = "Stop falling blocks from rendering.",
        category = "Performance", subcategory = "Entity Rendering"
    )
    public static boolean disableFallingBlocks;

    @Switch(
        title = "Disable End Portals",
        description = "Stop end portals from rendering.",
        category = "Performance", subcategory = "General"
    )
    public static boolean disableEndPortals;

    @Switch(
        title = "Unstacked Items",
        description = "Render stacks of items on the ground as just one instead of having up to 5 copies in one stack.",
        category = "Performance", subcategory = "Entity Rendering"
    )
    public static boolean unstackedItems;

    //@Info(
    //    text = "Due to the way OptiFine shaders work, we are unable to make Entity Culling compatible with them.",
    //    category = "Performance", subcategory = "Culling",
    //    type = InfoType.ERROR,
    //    size = 2
    //)
    //private static boolean entityCullingInfo = true;

    @Switch(
        title = "Entity Culling",
        description = "Check to see if an entity is visible to the player before attempting to render them.",
        category = "Performance", subcategory = "Culling"
    )
    public static boolean entityCulling = true;

    @Dropdown(
        title = "Entity Culling Interval",
        description = "The amount of time in ms between performing visibility checks for entities.\nShorter periods are more costly toward performance but provide the most accurate information.\nLower values are recommended in competitive environments.",
        category = "Performance", subcategory = "Culling",
        options = {"50", "25", "10"}
    )
    public static int cullingInterval = 0;

    @Switch(
        title = "Smart Entity Culling",
        description = "Disable Entity Culling effect when using OptiFine shaders.",
        category = "Performance", subcategory = "Culling"
    )
    public static boolean smartEntityCulling = true;

    @Switch(
        title = "Don't Cull Ender Dragons",
        description = "Continue to render Ender Dragons when the entity is being occluded.",
        category = "Performance", subcategory = "Culling"
    )
    public static boolean dontCullEnderDragons = true;

    @Switch(
        title = "Don't Cull Withers",
        description = "Continue to render Withers when the entity is being occluded.",
        category = "Performance", subcategory = "Culling"
    )
    public static boolean dontCullWithers = true;

    @Switch(
        title = "Don't Cull Player Nametags",
        description = "Continue to render Player Nametags when the entity is being occluded.",
        category = "Performance", subcategory = "Culling"
    )
    public static boolean dontCullNametags = true;

    @Switch(
        title = "Don't Cull Entity Nametags",
        description = "Continue to render Entity Nametags when the entity is being occluded.",
        category = "Performance", subcategory = "Culling"
    )
    public static boolean dontCullEntityNametags = true;

    @Switch(
        title = "Don't Cull Armorstand Nametags",
        description = "Continue to render Armorstand Nametags when the entity is being occluded.",
        category = "Performance", subcategory = "Culling"
    )
    public static boolean dontCullArmorStandNametags = true;

    @Switch(
        title = "Check Armorstand Rules",
        description = "Don't cull armor stands that have a specific rule assigned to them." +
            "\nThis will result in a lot of non-occluded armor stands in places like Hypixel Skyblock, " +
            "but will resolve special entities being occluded when they typically shouldn't be.",
        category = "Performance", subcategory = "Culling"
    )
    public static boolean checkArmorstandRules;

    @Switch(
        title = "Disable Enchantment Glint",
        description = "Disable the enchantment glint.",
        category = "Performance", subcategory = "General"
    )
    public static boolean disableEnchantmentGlint;

    //@Info(
    //    text = "When back-face culling is enabled, being inside an entity will cause that body part to be invisible.",
    //    category = "Performance", subcategory = "Culling",
    //    type = InfoType.WARNING,
    //    size = 2
    //)
    //private static boolean backFaceCullingInfo = true;
//
    //@Info(
    //    text = "Some models may have a transparent face and will cause the back face to not show, such as Wither Skeletons.",
    //    category = "Performance", subcategory = "Culling",
    //    type = InfoType.WARNING,
    //    size = 2
    //)
    //private static boolean backFaceCullingInfo2 = true;

    @Switch(
        title = "Entity Back-face Culling",
        description = "Stop rendering sides of entities that you cannot see.",
        category = "Performance", subcategory = "Culling"
    )
    public static boolean entityBackFaceCulling;

    @Switch(
        title = "Player Back-face Culling",
        description = "Stop rendering sides of players that you cannot see.\n" +
            "Being inside a player will cause that body part to be invisible.",
        category = "Performance", subcategory = "Culling"
    )
    public static boolean playerBackFaceCulling;

    @Switch(
        title = "Entity Render Distance Toggle",
        description = "Toggle allowing a custom entity render distance.",
        category = "Performance", subcategory = "Entity Rendering"
    )
    public static boolean entityRenderDistanceToggle;

    @Slider(
        title = "Global Entity Render Distance",
        description = "Stop rendering all entities outside of a specified radius.\n" +
            "This will ignore the distance of other entity render distances if smaller.",
        category = "Performance", subcategory = "Entity Rendering",
        min = 1, max = 64
    )
    public static int entityRenderDistance = 64;

    @Slider(
        title = "Player Render Distance",
        description = "Stop rendering players outside of a specified radius.",
        category = "Performance", subcategory = "Entity Rendering",
        min = 1, max = 64
    )
    public static int playerRenderDistance = 64;

    @Slider(
        title = "Passive Entity Render Distance",
        description = "Stop rendering passive entities outside of a specified radius.",
        category = "Performance", subcategory = "Entity Rendering",
        min = 1, max = 64
    )
    public static int passiveEntityRenderDistance = 64;

    @Slider(
        title = "Hostile Entity Render Distance",
        description = "Stop rendering hostile entities outside of a specified radius.",
        category = "Performance", subcategory = "Entity Rendering",
        min = 1, max = 64
    )
    public static int hostileEntityRenderDistance = 64;

    @Slider(
        title = "Tile Entity Render Distance",
        description = "Stop rendering tile entities outside of a specified radius.",
        category = "Performance", subcategory = "Entity Rendering",
        min = 1, max = 64
    )
    public static int tileEntityRenderDistance = 64;

    // SCREENS

    @Switch(
        title = "Fixed Inventory Position",
        description = "Stop potion effects from shifting your inventory to the right.",
        category = "Screens", subcategory = "Inventory"
    )
    @PreviousNames("Screens.Inventory.Inventory Position")
    public static boolean inventoryPosition = true;

    @Switch(
        title = "Click Out of Containers",
        description = "Click outside a container to close the menu.",
        category = "Screens", subcategory = "Inventory"
    )
    public static boolean clickOutOfContainers;

    @Dropdown(
        title = "Inventory Scale",
        description = "Change the scale of your inventory independent of your GUI scale.",
        category = "Screens", subcategory = "Inventory",
        options = {"Off", "1 (Small)", "2 (Normal)", "3 (Large)", "4", "5 (Auto)"}
    )
    public static int inventoryScale = 0;

    public static int getInventoryScale() {
        return inventoryScale == 0 ? -1 : inventoryScale;
    }

    @Slider(
        title = "Container Background Opacity (%)",
        description = "Change the opacity of the dark background inside a container, or remove it completely. By default, this is 81.5%.",
        category = "Screens", subcategory = "General",
        min = 0F, max = 100F
    )
    public static float containerBackgroundOpacity = (208/255F) * 100F;

    @Slider(
        title = "Container Opacity (%)",
        description = "Change the opacity of supported containers.\nIncludes Chests & Survival Inventory.",
        category = "Screens", subcategory = "General",
        min = 0F, max = 100F
    )
    public static float containerOpacity = 100F;

    //@Info(
    //    text = "Supported servers for 1.11 chat length are servers that support 1.11 or above.",
    //    category = "Screens", subcategory = "Chat",
    //    type = InfoType.WARNING,
    //    size = 2
    //)
    //private static boolean chatLengthInfo = true;
//
    //@Info(
    //    text = "Some servers may kick you for this despite supporting 1.11 or above.",
    //    category = "Screens", subcategory = "Chat",
    //    type = InfoType.WARNING,
    //    size = 2
    //)
    //private static boolean chatLengthInfo2 = true;

    @Switch(
        title = "1.11 Chat Length",
        description = "Extend the number of characters you can type from 100 to 256 on supported servers.",
        category = "Screens", subcategory = "Chat"
    )
    public static boolean extendedChatLength = true;

    @Switch(
        title = "Remove Chat Message Limit",
        description = "Remove the limit on how many messages can show up in chat.",
        category = "Screens", subcategory = "Chat"
    )
    public static boolean removeChatMessageLimit = true;

    @Switch(
        title = "Compact Chat",
        description = "Clean up the chat by stacking duplicate messages.",
        category = "Screens", subcategory = "Chat"
    )
    public static boolean compactChat = true;

    @Switch(
        title = "Consecutive Compact Chat",
        description = "Only compact messages if they're consecutive.",
        category = "Screens", subcategory = "Chat"
    )
    public static boolean consecutiveCompactChat;

    @Slider(
        title = "Compact Chat Time",
        description = "Change the amount of time old messages take to stop being compacted. Measured in seconds.",
        category = "Screens", subcategory = "Chat",
        min = 1, max = 120
    )
    public static int compactChatTime = 60;

    @Switch(
        title = "Remove Blank Messages",
        description = "Stop messages with no content from showing up in chat.",
        category = "Screens", subcategory = "Chat"
    )
    public static boolean removeBlankMessages;

    @Switch(
        title = "Shift Chat",
        description = "Keep chat open while sending a message if Shift is held while pressing Enter.",
        category = "Screens", subcategory = "Chat"
    )
    public static boolean shiftChat;

    @Slider(
        title = "Chat Delay",
        description = "Delay chat messages if they're sent within the selected timeframe after the previous message. Measured in seconds.",
        category = "Screens", subcategory = "Chat",
        min = 0, max = 6
    )
    public static int chatDelay = 0;

    @Switch(
        title = "Startup Notification",
        description = "Notify how long the game took to start.",
        category = "Screens", subcategory = "General"
    )
    public static boolean startupNotification = true;

    @Switch(
        title = "Damage Glance",
        description = "View the damage value of the currently held item above your hotbar.",
        category = "Screens", subcategory = "Combat Utilities"
    )
    public static boolean damageGlance;

    @Switch(
        title = "Item Count Glance",
        description = "View the total amount of the currently held item above your hotbar.",
        category = "Screens", subcategory = "Combat Utilities"
    )
    public static boolean itemCountGlance;

    @Switch(
        title = "Enchantment Glance",
        description = "View the enchantments of the currently held item above your hotbar.",
        category = "Screens", subcategory = "Combat Utilities"
    )
    public static boolean enchantmentsGlance;

    @Switch(
        title = "Protection Percentage",
        description = "View how much total armor protection you have inside your inventory.",
        category = "Screens", subcategory = "Combat Utilities"
    )
    public static boolean protectionPercentage;

    @Switch(
        title = "Projectile Protection Percentage",
        description = "View how much total projectile protection you have inside your inventory.",
        category = "Screens", subcategory = "Combat Utilities"
    )
    public static boolean projectileProtectionPercentage;

    @Switch(
        title = "Chat Timestamps",
        description = "Add timestamps before a message.",
        category = "Screens", subcategory = "Chat"
    )
    public static boolean timestamps;

    @Switch(
        title = "Show Seconds on Timestamps",
        description = "Show the seconds on a timestamped message.",
        category = "Screens", subcategory = "Chat"
    )
    public static boolean secondsOnTimestamps;

    @Dropdown(
        title = "Chat Timestamps Format",
        description = "Change the time format of Chat Timestamps.",
        category = "Screens", subcategory = "Chat",
        options = {"12 Hour", "24 Hour"}
    )
    public static int timestampsFormat = 0;

    @Dropdown(
        title = "Chat Timestamps Style",
        description = "Choose how Chat Timestamps should appear.",
        category = "Screens", subcategory = "Chat",
        options = {"Always Present", "Message Hover"}
    )
    public static int timestampsStyle = 0;

    @Switch(
        title = "Clean Main Menu",
        description = "Remove the Realms button on the main menu as it's useless on older versions.",
        category = "Screens", subcategory = "General"
    )
    public static boolean cleanMainMenu = true;

    @Dropdown(
        title = "Open to LAN Replacement",
        description = "Modify the Open to LAN button to either redirect to the server list or be removed.",
        category = "Screens", subcategory = "General",
        options = {"Default", "Server List", "Remove"}
    )
    public static int openToLanReplacement = 0;

    @Switch(
        title = "Image Preview",
        description = "Preview image links when hovering over a supported URL." +
            "\nPress Shift to use fullscreen and Control to render in native image resolution.",
        category = "Screens", subcategory = "Image Preview"
    )
    public static boolean imagePreview = true;

    @Slider(
        title = "Image Preview Width",
        description = "The %% of screen width to be used for image preview.",
        category = "Screens", subcategory = "Image Preview",
        min = 0F, max = 1F
    )
    public static float imagePreviewWidth = 0.50F;

    @Switch(
        title = "Safe Chat Clicks",
        description = "Show the command or link that is run/opened on click. ",
        category = "Screens", subcategory = "Chat"
    )
    public static boolean safeChatClicks;

    @Switch(
        title = "Safe Chat Clicks History",
        description = "Adds commands sent from clicking chat messages to the chat history.",
        category = "Screens", subcategory = "Chat"
    )
    public static boolean safeChatClicksHistory;

    @Switch(
        title = "Smart Disconnect",
        description = "Choose between disconnecting or relogging when clicking the disconnect button.\n§eOnly works on Multiplayer servers.",
        category = "Screens", subcategory = "General"
    )
    public static boolean smartDisconnect;

    @Switch(
        title = "Confirm Quit",
        description = "Prevent closing the game through the Quit Game button without confirmation.",
        category = "Screens", subcategory = "General"
    )
    public static boolean confirmQuit;

    // SCREENSHOTS

    //@Info(
    //    text = "Essential's Screenshot Manager must be disabled for this to work.",
    //    category = "Screenshots", subcategory = "General",
    //    type = InfoType.WARNING,
    //    size = 2
    //)
    //private static boolean screenshotManagerWarning = true;

    @Switch(
        title = "Screenshot Manager",
        description = "Change the way screenshotting works as a whole, creating a whole new process to screenshotting such as uploading to Imgur, copying to clipboard, etc.",
        category = "Screenshots", subcategory = "General"
    )
    public static boolean screenshotManager = false;

    @Switch(
        title = "No Feedback",
        description = "Remove the messages from screenshots entirely.",
        category = "Screenshots", subcategory = "Feedback"
    )
    public static boolean screenshotNoFeedback;

    @Switch(
        title = "Auto Copy Screenshot",
        description = "Automatically copy screenshots to the clipboard when taken.",
        category = "Screenshots", subcategory = "General"
    )
    public static boolean autoCopyScreenshot;

    @Switch(
        title = "Screenshot Preview",
        description = "Preview your screenshot when taken in the bottom right corner.",
        category = "Screenshots", subcategory = "General"
    )
    public static boolean screenshotPreview;

    @Slider(
        title = "Preview Time",
        description = "Adjust how long the preview should stay on the screen before sliding out.\nTime is measured in seconds.",
        category = "Screenshots", subcategory = "General",
        min = 1, max = 5
    )
    public static int previewTime = 3;

    @Dropdown(
        title = "Preview Animation",
        description = "Select an animation style for the screenshot preview.",
        category = "Screenshots", subcategory = "General",
        options = {"iOS Style", "Slide Out", "None"}
    )
    public static int previewAnimationStyle = 0;

    @Slider(
        title = "Preview Scale",
        description = "Change the scale of the preview.",
        category = "Screenshots", subcategory = "General",
        min = 0F, max = 1F
    )
    public static float previewScale = 1.0F;

    @Switch(
        title = "Compact Response",
        description = "Compact the message given when screenshotting.",
        category = "Screenshots", subcategory = "Feedback"
    )
    public static boolean compactScreenshotResponse;

    @Switch(
        title = "Favorite Screenshot",
        description = "Show a text component that allows you to favorite a screenshot.",
        category = "Screenshots", subcategory = "Feedback"
    )
    public static boolean favoriteScreenshot = true;

    @Switch(
        title = "Delete Screenshot",
        description = "Show a text component that allows you to delete a screenshot.",
        category = "Screenshots", subcategory = "Feedback"
    )
    public static boolean deleteScreenshot = true;

    @Switch(
        title = "Upload Screenshot",
        description = "Show a text component that allows you to upload a screenshot to Imgur.\nSupport for custom services is currently planned.",
        category = "Screenshots", subcategory = "Feedback"
    )
    public static boolean uploadScreenshot = true;

    @Switch(
        title = "Copy Screenshot",
        description = "Show a text component that allows you to copy a screenshot.",
        category = "Screenshots", subcategory = "Feedback"
    )
    public static boolean copyScreenshot = true;

    @Switch(
        title = "Open Screenshots Folder",
        description = "Show a text component that allows you to open the screenshots folder.",
        category = "Screenshots", subcategory = "Feedback"
    )
    public static boolean openScreenshotsFolder = true;

    // EXPERIMENTAL

    //@Info(
    //    text = "Requires two restarts to take full effect.",
    //    category = "Experimental", subcategory = "Forge",
    //    type = InfoType.WARNING
    //)
    //private static boolean cacheEntrypointsInfo = true;

    @Switch(
        title = "Entrypoint Caching",
        description = "Cache Forge mod entry points, improving startup time as Forge no longer needs to walk through " +
            "every class to find the @Mod annotation.",
        category = "Experimental", subcategory = "Forge"
    )
    public static boolean cacheEntrypoints = true;

    //todo this serializes for some reason?
    //@Button(
    //    title = "Reset Cache",
    //    description = "Reset the cache of Forge mod entry points.",
    //    category = "Experimental", subcategory = "Forge",
    //    text = "Reset"
    //)
    //public static Runnable resetCache = () -> EntrypointCaching.INSTANCE.resetCache();

    //@Info(
    //    text = "Improved Skin Rendering can make some skins invisible. It requires a restart once toggled.",
    //    category = "Experimental", subcategory = "Skin Rendering",
    //    type = InfoType.WARNING
    //)
    //private static boolean improvedSkinRenderingInfo;

    @Switch(
        title = "Improved Skin Rendering",
        description = "Remove transparent pixels on skins instead of turning them black.",
        category = "Experimental", subcategory = "Skin Rendering"
    )
    public static boolean improvedSkinRendering = false;

    //@Info(
    //    text = "This may cause stuff with animations to feel \"choppy\".",
    //    category = "Experimental", subcategory = "HUD Caching",
    //    type = InfoType.WARNING
    //)
    //private static boolean hudCachingWarning = true;

    @Switch(
        title = "HUD Caching",
        description = "Reuse frames from the HUD instead of constantly recreating them every frame, as most HUD elements will stay the same for a long amount of time.",
        category = "Experimental", subcategory = "HUD Caching"
    )
    public static boolean hudCaching;

    @Slider(
        title = "Cache FPS",
        description = "The amount of frames to cache for the HUD.",
        category = "Experimental", subcategory = "HUD Caching",
        step = 5,
        min = 5, max = 60
    )
    public static int cacheFPS = 20;

    // HIDDEN

    @Switch(
        title = "Nausea Effect",
        description = "Remove the nether portal effect appearing when clearing nausea.",
        category = "Miscellaneous", subcategory = "Overlays"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean nauseaEffectOld;
    @Slider(
        title = "Fire Overlay Opacity",
        description = "Change the opacity of the fire overlay.",
        category = "Miscellaneous", subcategory = "Overlays",
        min = 0F, max = 1.0F
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static float fireOverlayOpacityOld = 1F;
    @Switch(
        title = "Disable Titles",
        description = "Stop titles from appearing.",
        category = "Miscellaneous", subcategory = "Overlays"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean disableTitlesOld;
    @Slider(
        title = "Title Scale",
        description = "Set the scale for titles.",
        category = "Miscellaneous", subcategory = "Titles",
        min = 0F, max = 1F
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static float titleScaleOld = 1.0F;
    @Slider(
        title = "Title Opacity",
        description = "Change the opacity of titles.",
        category = "Miscellaneous", subcategory = "Titles",
        min = 0F, max = 1.0F
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static float titleOpacityOld = 1.0F;
    @Switch(
        title = "Toggle Tab",
        description = "Hold tab open without needing to hold down the tab key.",
        category = "Miscellaneous", subcategory = "Tab"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean toggleTabOld;
    @Switch(
        title = "Crosshair Perspective",
        description = "Remove the crosshair when in third person.",
        category = "Miscellaneous", subcategory = "General"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean crosshairPerspectiveOld;
    @Switch(
        title = "Show Own Nametag",
        description = "See your nametag in third person.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean showOwnNametagOld;
    @Slider(
        title = "Ridden Horse Opacity",
        description = "Change the opacity of the horse you're currently riding for visibility.",
        category = "Miscellaneous", subcategory = "Rendering",
        min = 0F, max = 100
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static float riddenHorseOpacityOld = 1F;
    @Switch(
        title = "Number Ping",
        description = "Show a readable ping number in tab instead of bars.",
        category = "Miscellaneous", subcategory = "Tab"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean numberPingOld;
    @Switch(
        title = "Clean View",
        description = "Stop rendering your potion effect particles.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean cleanViewOld;
    @Switch(
        title = "Disable Breaking Particles",
        description = "Remove block-breaking particles for visibility.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean disableBlockBreakParticlesOld;
    @Switch(
        title = "Remove Inverted Colors from Crosshair",
        description = "Remove the inverted color effect on the crosshair.",
        category = "Miscellaneous", subcategory = "Overlays"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean removeInvertFromCrosshairOld;
    @Switch(
        title = "Add Text Shadow to Nametags",
        description = "Render nametags with shadowed text.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean shadowedNametagTextOld;
    @Switch(
        title = "Add Text Shadow to Actionbar",
        description = "Render actionbar messages with shadowed text.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean shadowedActionbarTextOld;
    @Switch(
        title = "Add Background to Actionbar",
        description = "Render a background behind the actionbar.",
        category = "Miscellaneous", subcategory = "Rendering"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean actionbarBackgroundOld;
    @Switch(
        title = "Remove Vertical Bobbing",
        description = "While using View Bobbing, remove the vertical bobbing like in 1.14+.",
        category = "Miscellaneous", subcategory = "General"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean removeVerticalViewBobbingOld;
    @Switch(
        title = "Static Particle Color",
        description = "Disable particle lighting checks each frame.",
        category = "Performance", subcategory = "Particles"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean staticParticleColorOld = false;

    @Slider(
        title = "Max Particle Limit",
        description = "Stop additional particles from appearing when there are too many at once.",
        category = "Performance", subcategory = "Particles",
        min = 1, max = 10000
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static int maxParticleLimitOld = 4000;
    @Switch(
        title = "Disable Nametag Boxes",
        description = "Remove the transparent box around the nametag.",
        category = "Performance", subcategory = "Entity Rendering"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean disableNametagBoxesOld;
    @Switch(
        title = "Remove Container Background",
        description = "Remove the dark background inside a container.",
        category = "Screens", subcategory = "General"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean removeContainerBackgroundOld = false;
    @Switch(
        title = "GUI Crosshair",
        description = "Stop rendering the crosshair when in a GUI.",
        category = "Screens", subcategory = "General"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean guiCrosshairOld;

    @Slider(
        title = "Tab Opacity",
        description = "Change the tab list opacity.",
        category = "Screens", subcategory = "Tab",
        min = 0F, max = 1.0F
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static float tabOpacityOld = 1.0F;

    @Slider(
        title = "Tab Player Count",
        description = "Change how many players can display on tab.",
        category = "Screens", subcategory = "Tab",
        min = 10, max = 120
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static int tabPlayerCountOld = 80;
    @Switch(
        title = "Tab Height",
        description = "Move the tab overlay down the selected amount of pixels when there's an active bossbar.",
        category = "Screens", subcategory = "Tab"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean tabHeightAllowOld;

    @Slider(
        title = "Set Tab Height",
        description = "Choose how many pixels tab will move down when there's an active bossbar.",
        category = "Screens", subcategory = "Tab",
        min = 0, max = 24
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static int tabHeightOld = 10;

    @Switch(
        title = "Fix Actionbar Overlap",
        description = "Prevents the actionbar text from rendering above the armor/health bar.",
        category = "Bug Fixes", subcategory = "Rendering"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean fixActionbarOverlapOld;

    @Switch(
        title = "Transparent Chat",
        description = "Remove the background from chat.",
        category = "Screens", subcategory = "Chat"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean transparentChatOld;

    @Switch(
        title = "Chat Background When Open",
        description = "Add back the background when chat is open.",
        category = "Screens", subcategory = "Chat"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean transparentChatOnlyWhenClosedOld;

    @Switch(
        title = "Transparent Chat Input Field",
        description = "Remove the background from chat's input field.",
        category = "Screens", subcategory = "Chat"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean transparentChatInputFieldOld;

    @Switch(
        title = "Extend Chat Background",
        description = "Extend the chat background all the way to the left of the screen.",
        category = "Screens", subcategory = "Chat"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean extendChatBackgroundOld;

    @Switch(
        title = "Chat Position",
        description = "Move the chat up 12 pixels to stop it from overlapping the health bar, as done in 1.12+.",
        category = "Screens", subcategory = "Chat"
    )
    // HIDDEN OPTION!!!!!!! DO NOT REMOVE OR TOUCH
    public static boolean chatPositionOld;

    //@Info(
    //    text = "These options have been replaced by other mods. Hover over the option name to see which mod replaces that feature.",
    //    category = "Deprecated",
    //    type = InfoType.WARNING,
    //    size = 2
    //)
    //private static boolean deprecatedInfo = true;

    public static boolean nauseaEffect = false;
    public static float fireOverlayOpacity = 1F;
    @Switch(
        title = "Disable Titles",
        description = "Stop titles from appearing.",
        category = "Deprecated", subcategory = "Titles"
    )
    public static boolean disableTitles = false;
    @Slider(
        title = "Title Scale",
        description = "Set the scale for titles.",
        category = "Deprecated", subcategory = "Titles",
        min = 0F, max = 1F
    )
    public static float titleScale = 1.0F;
    @Slider(
        title = "Title Opacity",
        description = "Change the opacity of titles.",
        category = "Deprecated", subcategory = "Titles",
        min = 0F, max = 1.0F
    )
    public static float titleOpacity = 1.0F;
    @Switch(
        title = "Toggle Tab",
        description = "Hold tab open without needing to hold down the tab key.",
        category = "Deprecated", subcategory = "Tab"
    )
    public static boolean toggleTab = false;
    @Switch(
        title = "Crosshair Perspective",
        description = "Remove the crosshair when in third person.",
        category = "Deprecated", subcategory = "Crosshair"
    )
    public static boolean crosshairPerspective = false;
    @Switch(
        title = "Remove Inverted Colors from Crosshair",
        description = "Remove the inverted color effect on the crosshair.",
        category = "Deprecated", subcategory = "Crosshair"
    )
    public static boolean removeInvertFromCrosshair = false;
    @Switch(
        title = "Show Own Nametag",
        description = "See your nametag in third person.",
        category = "Deprecated", subcategory = "Nametags"
    )
    public static boolean showOwnNametag = false;
    @Switch(
        title = "Add Text Shadow to Nametags",
        description = "Render nametags with shadowed text.",
        category = "Deprecated", subcategory = "Nametags"
    )
    public static boolean shadowedNametagText = false;
    public static float riddenHorseOpacity = 1F;
    @Switch(
        title = "Number Ping",
        description = "Show a readable ping number in tab instead of bars.",
        category = "Deprecated", subcategory = "Tab"
    )
    public static boolean numberPing;
    @Switch(
        title = "Clean View",
        description = "Stop rendering your potion effect particles.",
        category = "Deprecated", subcategory = "Particles"
    )
    public static boolean cleanView = false;
    @Switch(
        title = "Disable Breaking Particles",
        description = "Remove block-breaking particles for visibility.",
        category = "Deprecated", subcategory = "Particles"
    )
    public static boolean disableBlockBreakParticles = false;
    @Switch(
        title = "Add Text Shadow to Actionbar",
        description = "Render actionbar messages with shadowed text.",
        category = "Deprecated", subcategory = "Actionbar"
    )
    public static boolean shadowedActionbarText = false;
    @Switch(
        title = "Add Background to Actionbar",
        description = "Render a background behind the actionbar.",
        category = "Deprecated", subcategory = "Actionbar"
    )
    public static boolean actionbarBackground = false;
    @Switch(
        title = "Remove Vertical Bobbing",
        description = "While using View Bobbing, remove the vertical bobbing like in 1.14+.",
        category = "Deprecated", subcategory = "Animations"
    )
    public static boolean removeVerticalViewBobbing = false;
    @Switch(
        title = "Static Particle Color",
        description = "Disable particle lighting checks each frame.",
        category = "Deprecated", subcategory = "Particles"
    )
    public static boolean staticParticleColor = false;
    @Slider(
        title = "Max Particle Limit",
        description = "Stop additional particles from appearing when there are too many at once.",
        category = "Deprecated", subcategory = "Particles",
        min = 1, max = 10000
    )
    public static int maxParticleLimit = 4000;
    @Switch(
        title = "Disable Nametag Boxes",
        description = "Remove the transparent box around the nametag.",
        category = "Deprecated", subcategory = "Nametags"
    )
    public static boolean disableNametagBoxes = false;
    public static boolean removeContainerBackground = false;
    @Switch(
        title = "GUI Crosshair",
        description = "Stop rendering the crosshair when in a GUI.",
        category = "Deprecated", subcategory = "Crosshair"
    )
    public static boolean guiCrosshair = false;
    @Slider(
        title = "Tab Opacity",
        description = "Change the tab list opacity.",
        category = "Deprecated", subcategory = "Tab",
        min = 0F, max = 1.0F
    )
    public static float tabOpacity = 1.0F;
    @Slider(
        title = "Tab Player Count",
        description = "Change how many players can display on tab.",
        category = "Deprecated", subcategory = "Tab",
        min = 10, max = 120
    )
    public static int tabPlayerCount = 80;
    @Switch(
        title = "Tab Height",
        description = "Move the tab overlay down the selected amount of pixels when there's an active bossbar.",
        category = "Deprecated", subcategory = "Tab"
    )
    public static boolean tabHeightAllow = false;
    @Slider(
        title = "Set Tab Height",
        description = "Choose how many pixels tab will move down when there's an active bossbar.",
        category = "Deprecated", subcategory = "Tab",
        min = 0, max = 24
    )
    public static int tabHeight = 0;
    @Switch(
        title = "Fix Actionbar Overlap",
        description = "Prevents the actionbar text from rendering above the armor/health bar.",
        category = "Deprecated", subcategory = "Actionbar"
    )
    public static boolean fixActionbarOverlap = false;
    @Switch(
        title = "Transparent Chat",
        description = "Remove the background from chat.",
        category = "Deprecated", subcategory = "Chat"
    )
    public static boolean transparentChat;
    @Switch(
        title = "Chat Background When Open",
        description = "Add back the background when chat is open.",
        category = "Deprecated", subcategory = "Chat"
    )
    public static boolean transparentChatOnlyWhenClosed;
    @Switch(
        title = "Transparent Chat Input Field",
        description = "Remove the background from chat's input field.",
        category = "Deprecated", subcategory = "Chat"
    )
    public static boolean transparentChatInputField;
    @Switch(
        title = "Extend Chat Background",
        description = "Extend the chat background all the way to the left of the screen.",
        category = "Deprecated", subcategory = "Chat"
    )
    public static boolean extendChatBackground = false;
    @Switch(
        title = "Chat Position",
        description = "Move the chat up 12 pixels to stop it from overlapping the health bar, as done in 1.12+.",
        category = "Deprecated", subcategory = "Chat"
    )
    public static boolean chatPosition = false;


    public static boolean labyModMoment = true;
    @Include public static String selectedAudioDevice = "";

    public static PatcherConfig INSTANCE = new PatcherConfig(); // Needs to be at the bottom or the default values take priority

    public PatcherConfig() {
        super("patcher.json", "/patcher.svg", "PolyPatcher", Category.QOL);
        //super(new Mod("PolyPatcher", ModType.UTIL_QOL, "/patcher.svg", new VigilanceMigrator("./config/patcher.toml")), "patcher.json");

        boolean modified = false;

        if (removeContainerBackgroundOld) {
            containerBackgroundOpacity = 0F;
            modified = true;
        }
        if (nauseaEffectOld) {
            distortionEffect = 0;
            modified = true;
        }

        if (fireOverlayOpacityOld != 1) {
            fireOverlayOpacityI = (int) (fireOverlayOpacityOld * 100);
            riddenHorseOpacityI = (int) (riddenHorseOpacityOld * 100);
            modified = true;
        }

        //todo uhhh
        //if (modified) {
        //    try {
        //        FileUtils.writeStringToFile(ConfigUtils.getProfileFile("patcher-not-migrated.json"), FileUtils.readFileToString(ConfigUtils.getProfileFile("patcher.json"), StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        //    } catch (IOException e) {
        //        Patcher.getLogger().error("Failed to copy over Patcher config before migration.", e);
        //    }
        //    save();
        //}

        Runnable reloadWorld = () -> Minecraft.getMinecraft().renderGlobal.loadRenderers();
        addCallback("fullbright", reloadWorld);
        addCallback("removeGroundFoliage", reloadWorld);
        addCallback("vanillaGlassPanes", reloadWorld);

        Runnable reloadTextures = () -> Minecraft.getMinecraft().refreshResources();
        addCallback("heldItemLighting", reloadTextures);

        //todo lol
        //hideIf("nauseaEffectOld", () -> true);
        //hideIf("fireOverlayOpacityOld", () -> true);
        //hideIf("disableTitlesOld", () -> true);
        //hideIf("titleScaleOld", () -> true);
        //hideIf("titleOpacityOld", () -> true);
        //hideIf("toggleTabOld", () -> true);
        //hideIf("crosshairPerspectiveOld", () -> true);
        //hideIf("showOwnNametagOld", () -> true);
        //hideIf("riddenHorseOpacityOld", () -> true);
        //hideIf("numberPingOld", () -> true);
        //hideIf("cleanViewOld", () -> true);
        //hideIf("disableBlockBreakParticlesOld", () -> true);
        //hideIf("removeInvertFromCrosshairOld", () -> true);
        //hideIf("shadowedNametagTextOld", () -> true);
        //hideIf("shadowedActionbarTextOld", () -> true);
        //hideIf("actionbarBackgroundOld", () -> true);
        //hideIf("removeVerticalViewBobbingOld", () -> true);
        //hideIf("staticParticleColorOld", () -> true);
        //hideIf("maxParticleLimitOld", () -> true);
        //hideIf("disableNametagBoxesOld", () -> true);
        //hideIf("removeContainerBackgroundOld", () -> true);
        //hideIf("guiCrosshairOld", () -> true);
        //hideIf("tabOpacityOld", () -> true);
        //hideIf("tabPlayerCountOld", () -> true);
        //hideIf("tabHeightAllowOld", () -> true);
        //hideIf("tabHeightOld", () -> true);
        //hideIf("fixActionbarOverlapOld", () -> true);
        //hideIf("transparentChatOld", () -> true);
        //hideIf("transparentChatOnlyWhenClosedOld", () -> true);
        //hideIf("transparentChatInputFieldOld", () -> true);
        //hideIf("extendChatBackgroundOld", () -> true);
        //hideIf("chatPositionOld", () -> true);

        OldPatcherConfig.nauseaEffect = nauseaEffectOld;
        OldPatcherConfig.fireOverlayOpacity = fireOverlayOpacityOld;
        OldPatcherConfig.disableTitles = disableTitlesOld;
        OldPatcherConfig.titleScale = titleScaleOld;
        OldPatcherConfig.titleOpacity = titleOpacityOld;
        OldPatcherConfig.toggleTab = toggleTabOld;
        OldPatcherConfig.crosshairPerspective = crosshairPerspectiveOld;
        OldPatcherConfig.showOwnNametag = showOwnNametagOld;
        OldPatcherConfig.riddenHorseOpacity = riddenHorseOpacityOld;
        OldPatcherConfig.numberPing = numberPingOld;
        OldPatcherConfig.cleanView = cleanViewOld;
        OldPatcherConfig.disableBlockBreakParticles = disableBlockBreakParticlesOld;
        OldPatcherConfig.removeInvertFromCrosshair = removeInvertFromCrosshairOld;
        OldPatcherConfig.shadowedNametagText = shadowedNametagTextOld;
        OldPatcherConfig.shadowedActionbarText = shadowedActionbarTextOld;
        OldPatcherConfig.actionbarBackground = actionbarBackgroundOld;
        OldPatcherConfig.removeVerticalViewBobbing = removeVerticalViewBobbingOld;
        OldPatcherConfig.staticParticleColor = staticParticleColorOld;
        OldPatcherConfig.maxParticleLimit = maxParticleLimitOld;
        OldPatcherConfig.disableNametagBoxes = disableNametagBoxesOld;
        OldPatcherConfig.removeContainerBackground = removeContainerBackgroundOld;
        OldPatcherConfig.guiCrosshair = guiCrosshairOld;
        OldPatcherConfig.tabOpacity = tabOpacityOld;
        OldPatcherConfig.tabPlayerCount = tabPlayerCountOld;
        OldPatcherConfig.tabHeightAllow = tabHeightAllowOld;
        OldPatcherConfig.tabHeight = tabHeightOld;
        OldPatcherConfig.fixActionbarOverlap = fixActionbarOverlapOld;
        OldPatcherConfig.transparentChat = transparentChatOld;
        OldPatcherConfig.transparentChatOnlyWhenClosed = transparentChatOnlyWhenClosedOld;
        OldPatcherConfig.transparentChatInputField = transparentChatInputFieldOld;
        OldPatcherConfig.extendChatBackground = extendChatBackgroundOld;
        OldPatcherConfig.chatPosition = chatPositionOld;

        addDependency("disableTitles", "Replaced by VanillaHUD. Please install VanillaHUD to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("titleScale", "Replaced by VanillaHUD. Please install VanillaHUD to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("titleOpacity", "Replaced by VanillaHUD. Please install VanillaHUD to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("toggleTab", "Replaced by VanillaHUD. Please install VanillaHUD to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("crosshairPerspective", "Replaced by PolyCrosshair. Please install PolyCrosshair to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("showOwnNametag", "Replaced by PolyNametag. Please install PolyNametag to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("numberPing", "Replaced by VanillaHUD. Please install VanillaHUD to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("cleanView", "Replaced by OverflowParticles. Please install OverflowParticles to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("disableBlockBreakParticles", "Replaced by OverflowParticles. Please install OverflowParticles to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("removeInvertFromCrosshair", "Replaced by PolyCrosshair. Please install PolyCrosshair to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("shadowedNametagText", "Replaced by PolyNametag. Please install PolyNametag to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("shadowedActionbarText", "Replaced by VanillaHUD. Please install VanillaHUD to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("actionbarBackground", "Replaced by VanillaHUD. Please install VanillaHUD to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("removeVerticalViewBobbing", "Replaced by OverflowAnimations. Please install OverflowAnimations to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("staticParticleColor", "Replaced by OverflowParticles. Please install OverflowParticles to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("maxParticleLimit", "Replaced by OverflowParticles. Please install OverflowParticles to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("disableNametagBoxes", "Replaced by PolyNametag. Please install PolyNametag to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("guiCrosshair", "Replaced by PolyCrosshair. Please install PolyCrosshair to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("tabOpacity", "Replaced by VanillaHUD. Please install VanillaHUD to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("tabPlayerCount", "Replaced by VanillaHUD. Please install VanillaHUD to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("tabHeightAllow", "Replaced by VanillaHUD. Please install VanillaHUD to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("tabHeight", "Replaced by VanillaHUD. Please install VanillaHUD to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("fixActionbarOverlap", "Replaced by VanillaHUD. Please install VanillaHUD to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("transparentChat", "Replaced by Chatting. Please install Chatting to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("transparentChatOnlyWhenClosed", "Replaced by Chatting. Please install Chatting to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("transparentChatInputField", "Replaced by Chatting. Please install Chatting to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("extendChatBackground", "Replaced by Chatting. Please install Chatting to use this feature.", () -> Property.Display.HIDDEN);
        addDependency("chatPosition", "Replaced by Chatting. Please install Chatting to use this feature.", () -> Property.Display.HIDDEN);

        try {
            addDependency("cactusHitboxExclusion", "futureHitBoxes");
            addDependency("smartFullbright", "fullbright");
            addDependency("cleanerNightVision", "disableNightVision", () -> disableNightVision ? Property.Display.HIDDEN : Property.Display.SHOWN);
            addDependency("unfocusedFPSAmount", "unfocusedFPS");
            addDependency("instantFullscreen", "windowedFullscreen");
            addDependency("consecutiveCompactChat", "compactChat");
            addDependency("compactChatTime", "compactChat");
            addDependency("timestampsFormat", "timestamps");
            addDependency("timestampsStyle", "timestamps");
            addDependency("secondsOnTimestamps", "timestamps");
            addDependency("imagePreviewWidth", "imagePreview");

            Arrays.asList(
                "slownessFovModifierFloat", "speedFovModifierFloat",
                "bowFovModifierFloat", "sprintingFovModifierFloat",
                "flyingFovModifierFloat"
            ).forEach(property -> addDependency(property, "allowFovModifying"));

            addDependency("logOptimizerLength", "logOptimizer");
            addDependency("dynamicZoomSensitivity", "scrollToZoom");
            addDependency("smoothZoomAnimation", "scrollToZoom");
            addDependency("smoothZoomAlgorithm", "smoothZoomAnimation");

            Arrays.asList(
                "cullingInterval", "smartEntityCulling", "dontCullNametags",
                "dontCullEntityNametags", "dontCullArmorStandNametags", "checkArmorstandRules"
            ).forEach(property -> addDependency(property, "entityCulling"));

            Arrays.asList(
                "entityRenderDistance", "playerRenderDistance",
                "passiveEntityRenderDistance", "hostileEntityRenderDistance",
                "tileEntityRenderDistance"
            ).forEach(property -> addDependency(property, "entityRenderDistanceToggle"));

            addDependency("cacheFontData", "optimizedFontRenderer");
            addDependency("chunkUpdateLimit", "limitChunks");

            Arrays.asList(
                "screenshotNoFeedback", "compactScreenshotResponse", "autoCopyScreenshot", "screenshotPreview",
                "previewTime", "previewAnimationStyle", "previewScale", "favoriteScreenshot",
                "deleteScreenshot", "uploadScreenshot", "copyScreenshot", "openScreenshotsFolder"
            ).forEach(property -> addDependency(property, "screenshotManager"));

            //hideIf("instantFullscreen", () -> !SystemUtils.IS_OS_WINDOWS);

            Supplier<Boolean> noOptiFine = () -> ClassTransformer.optifineVersion.equals("NONE");
            //Arrays.asList(
            //    "scrollToZoom", "normalZoomSensitivity", "customZoomSensitivity", "smoothZoomAnimation",
            //    "smoothZoomAnimationWhenScrolling", "smoothZoomAlgorithm", "toggleToZoom", "normalFpsCounter",
            //    "useVanillaMetricsRenderer", "renderHandWhenZoomed", "smartFullbright", "smartEntityCulling",
            //    "dynamicZoomSensitivity", "customSkyFix"
            //).forEach(property -> hideIf(property, noOptiFine));

            Supplier<Boolean> smoothFontDetected = () -> ClassTransformer.smoothFontDetected;
            //hideIf("optimizedFontRenderer", smoothFontDetected);
            //hideIf("cacheFontData", smoothFontDetected);

            // these are all already built into the client by 1.12, so no
            // need to show them in the config menu

            //noinspection ConstantConditions
            //Supplier<Boolean> minecraft112 = () -> ForgeVersion.mcVersion.equals("1.12.2");
            //Arrays.asList(
            //    "resourceExploitFix", "newKeybindHandling", "separateResourceLoading", "futureHitBoxes", "cactusHitboxExclusion", "farmSelectionBoxesInfo", "cactusHitboxExclusionInfo",
            //    "leftHandInFirstPerson", "extendedChatLength", "chatPosition",
            //    "parallaxFix", "extendChatBackground", "vanillaGlassPanes", "heldItemLighting"
            //).forEach(property -> hideIf(property, minecraft112));

            //hideIf("keyboardLayout", () -> !SystemUtils.IS_OS_LINUX);
        } catch (Exception e) {
            Patcher.getLogger().error("Failed to access property.", e);
        }
    }

    // compat with vigilance
    public void markDirty() {

    }

    public void writeData() {
        save();
    }
}
