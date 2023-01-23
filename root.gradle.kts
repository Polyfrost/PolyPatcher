plugins {
    kotlin("jvm") version "1.6.0" apply false
    id("cc.polyfrost.multi-version.root")
}

// normal versions will be "1.x.x"
// betas will be "1.x.x+beta-y" / "1.x.x+branch_beta-y"
// rcs will be 1.x.x+rc-y
// extra branches will be 1.x.x+branch-y
version = "1.8.6+oneconfig_alpha-2"

preprocess {
    val forge10809 = createNode("1.8.9-forge", 10809, "srg")
    val fabric10809 = createNode("1.8.9-fabric", 10809, "yarn")
    val forge11202 = createNode("1.12.2-forge", 11202, "srg")
    val fabric11202 = createNode("1.12.2-fabric", 11202, "yarn")

    fabric11202.link(fabric10809)
    forge11202.link(forge10809, file("1.12.2-1.8.9.txt"))
    fabric10809.link(forge10809, file("fabric-forge-legacy.txt"))
}
