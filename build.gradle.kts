@file:Suppress("UnstableApiUsage", "PropertyName")

import dev.deftu.gradle.utils.GameSide

plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.resources")
    id("dev.deftu.gradle.tools.bloom")
    id("dev.deftu.gradle.tools.shadow")
    id("dev.deftu.gradle.tools.minecraft.loom")
}

val accessTransformerName = "patcher1${mcData.version.minor}_at.cfg"

// Sets up the variables for when we preprocess to other Minecraft versions.
preprocess {
    vars.put("MODERN", if (mcData.version.minor >= 16) 1 else 0)
}

//if (project.platform.isLegacyForge) {
//    runConfigs {
//        "client" {
//            property("patcher.debugBytecode", "true")
//            property("mixin.debug.verbose", "true")
//            property("mixin.debug.export", "true")
//            property("mixin.dumpTargetOnFailure", "true")
//            property("fml.coreMods.load", "club.sk1er.patcher.tweaker.PatcherTweaker")
//            programArgs("--tweakClass", "org.polyfrost.oneconfig.internal.legacy.OneConfigTweaker")
//            programArgs("--tweakClass", "org.spongepowered.asm.launch.MixinTweaker")
//            property("mixin.debug.export", "true")
//            programArgs("--mixin", "mixins.${mod_id}.json")
//        }
//    }
//}

//if (project.platform.isForge) {
//    forge {
//        accessTransformer(rootProject.file("src/main/resources/$accessTransformerName"))
//        mixinConfig("mixins.${mod_id}.json")
//    }
//}

toolkitLoomHelper {
    useOneConfig("1.1.0-alpha.34", "1.0.0-alpha.43", mcData, "commands", "config-impl", "events", "hud", "internal", "ui")
    useDevAuth()

    disableRunConfigs(GameSide.SERVER)

    if (!mcData.isNeoForge) {
        useMixinRefMap(modData.id)
    }

    if (mcData.isLegacyForge) {
        useTweaker("org.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker", GameSide.CLIENT)
        useForgeMixin(modData.id) // Configures the mixins if we are building for forge, useful for when we are dealing with cross-platform projects.

        useProperty("patcher.debugBytecode", "true", GameSide.CLIENT)
        useProperty("mixin.debug.verbose", "true", GameSide.CLIENT)
        useProperty("mixin.debug.export", "true", GameSide.CLIENT)
        useProperty("mixin.dumpTargetOnFailure", "true", GameSide.CLIENT)
        useProperty("fml.coreMods.load", "club.sk1er.patcher.tweaker.PatcherTweaker", GameSide.CLIENT)
    }
}

if (mcData.isForge) {
    loom {
        forge {
            accessTransformer(rootProject.file("src/main/resources/$accessTransformerName"))
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
    modImplementation(shade("org.polyfrost:elementa-$mcData:562") {
        isTransitive = false
    })

    implementation(shade("com.github.ben-manes.caffeine:caffeine:2.9.3")!!)

    implementation(shade("com.github.videogame-hacker:Koffee:88ba1b0") {
        isTransitive = false
    })

    if (mcData.version.minor < 12) {
        implementation(shade("it.unimi.dsi:fastutil:8.5.13")!!)
    }

    // If we are building for legacy forge, includes the launch wrapper with `shade` as we configured earlier.
    if (mcData.isLegacyForge) {
        compileOnly("org.polyfrost:polymixin:0.8.4+build.2")
        //todo fix with V1
        //modImplementation("org.polyfrost:legacy-crafty-crashes:1.0.0") {
        //    isTransitive = false
        //}
    }
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xno-param-assertions", "-Xjvm-default=all-compatibility")
        }
    }

    jar {
        // Sets the jar manifest attributes.
        if (mcData.isLegacyForge) {
            manifest.attributes += mapOf(
                "FMLAT" to accessTransformerName,
                "Main-Class" to "club.sk1er.container.ContainerMessage"
            )
        }
    }

    processResources {
        exclude("patcher18_at.cfg")
        exclude("patcher112_at.cfg")
    }
}