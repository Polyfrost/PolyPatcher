plugins {
    id("dev.deftu.gradle.multiversion-root")
}

preprocess {
    strictExtraMappings.set(true)
    "1.12.2-forge"(11202, "srg") {
        "1.12.2-fabric"(11202, "yarn") {
            "1.8.9-fabric"(10809, "yarn", file("versions/1.12.2-1.8.9.txt")) {
                "1.8.9-forge"(10809, "srg")
            }
        }
    }
}