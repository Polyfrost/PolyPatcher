@file:Suppress("UnstableApiUsage", "PropertyName")

import dev.deftu.gradle.utils.GameSide
import dev.deftu.gradle.utils.includeOrShade

plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.multiversion") // Applies preprocessing for multiple versions of Minecraft and/or multiple mod loaders.
    id("dev.deftu.gradle.tools") // Applies several configurations to things such as the Java version, project name/version, etc.
    id("dev.deftu.gradle.tools.resources") // Applies resource processing so that we can replace tokens, such as our mod name/version, in our resources.
    id("dev.deftu.gradle.tools.bloom") // Applies the Bloom plugin, which allows us to replace tokens in our source files, such as being able to use `@MOD_VERSION` in our source files.
    id("dev.deftu.gradle.tools.shadow") // Applies the Shadow plugin, which allows us to shade our dependencies into our mod JAR. This is NOT recommended for Fabric mods, but we have an *additional* configuration for those!
    id("dev.deftu.gradle.tools.minecraft.loom") // Applies the Loom plugin, which automagically configures Essential's Architectury Loom plugin for you.
    id("dev.deftu.gradle.tools.minecraft.releases") // Applies the Minecraft auto-releasing plugin, which allows you to automatically release your mod to CurseForge and Modrinth.
}

val accessTransformerName = "patcher1${mcData.version.minor}_at.cfg"

toolkitLoomHelper {
    useOneConfig {
        version = "1.0.0-alpha.49"
        loaderVersion = "1.1.0-alpha.35"

        usePolyMixin = true
        polyMixinVersion = "0.8.4+build.2"

        applyLoaderTweaker = true

        for (module in arrayOf("commands", "config", "config-impl", "events", "internal", "ui", "utils")) {
            +module
        }
    }
    useDevAuth("1.2.1")
    useMixinExtras("0.4.1")

    // Turns off the server-side run configs, as we're building a client-sided mod.
    disableRunConfigs(GameSide.SERVER)

    // Defines the name of the Mixin refmap, which is used to map the Mixin classes to the obfuscated Minecraft classes.
    if (!mcData.isNeoForge) {
        useMixinRefMap(modData.id)
    }

    if (mcData.isForge) {
        // Configures the Mixin tweaker if we are building for Forge.
        useForgeMixin(modData.id)

        useProperty("patcher.debugBytecode", "true", GameSide.CLIENT)
        useProperty("fml.coreMods.load", "club.sk1er.patcher.tweaker.PatcherTweaker", GameSide.CLIENT)
    }
}

if (mcData.isForge) {
    loom {
        forge {
            accessTransformer(rootProject.file("src/main/resources/META-INF/$accessTransformerName"))
        }
    }
}

// Configures the output directory for when building from the `src/resources` directory.
sourceSets {
    val dummy by creating
    main {
        dummy.compileClasspath += compileClasspath
        compileClasspath += dummy.output
        output.setResourcesDir(java.classesDirectory)
    }
}

// Configures the libraries/dependencies for your mod.
dependencies {
    modImplementation(includeOrShade("org.polyfrost:elementa-$mcData:562") {
        isTransitive = false
    })

    implementation(includeOrShade("com.github.ben-manes.caffeine:caffeine:2.9.3")!!)

    implementation(includeOrShade("com.github.char:Koffee:88ba1b0") {
        isTransitive = false
    })

    if (mcData.version.minor < 12) {
        implementation(includeOrShade("it.unimi.dsi:fastutil:8.5.13")!!)
    }
    if (mcData.isFabric) {
        runtimeOnly("include"(rootProject.project(":fake-mod"))!!)
        implementation(includeOrShade("com.github.Chocohead:Fabric-ASM:2.3")!!)
    }
}

tasks {
    compileKotlin {
        compilerOptions {
            optIn.add("kotlin.RequiresOptIn")
            freeCompilerArgs.add("-Xno-param-assertions")
            freeCompilerArgs.add("-Xjvm-default=all-compatibility")
        }
    }

    jar {
        // Sets the jar manifest attributes.
        if (mcData.isLegacyForge) {
            manifest.attributes += mapOf(
                "FMLCorePlugin" to "club.sk1er.patcher.tweaker.PatcherTweaker",
                "FMLCorePluginContainsFMLMod" to "Yes, yes it does",
                "FMLAT" to accessTransformerName,
                "Main-Class" to "club.sk1er.container.ContainerMessage"
            )
        }
    }

    processResources {
        exclude("**/*.at.cfg")
    }
}