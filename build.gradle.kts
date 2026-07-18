import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    `java-library`
    alias(libs.plugins.lombok)
    alias(libs.plugins.plugin.yml)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.shadow)
}

repositories {
    mavenCentral()

    maven("https://maven.buildtheearth.net/releases")

    exclusiveContent {
        forRepository {
            maven {
                url = uri("https://repo.papermc.io/repository/maven-public/")
            }
        }
        filter {
            includeGroup("io.papermc")
            includeGroup("io.papermc.paper")
            includeGroup("net.md-5")
            includeGroup("com.mojang")
        }
    }

    maven("https://repo.lushplugins.org/releases")
}

dependencies {
    paperLibrary(libs.terraminusminus)
    paperLibrary(libs.daporkchop.lib.common)
    implementation(libs.bstats)
    paperLibrary(libs.pluginupdater.common) {
        exclude(group = "com.google.guava", module = "guava")
    }
    paperLibrary(libs.jspecify)
    paperLibrary(libs.pluginupdater.paper)
    compileOnly(libs.paper.api)
    compileOnly(libs.jackson.databind)
    compileOnly(libs.jetbrains.annotations)
}

group = "de.btegermany"
version = "1.7.2-SNAPSHOT"
description = "A plugin which implements the terra-- api in a paper plugin"
java.sourceCompatibility = JavaVersion.VERSION_21

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

paper {
    website = "https://bte-germany.de"

    main = "de.btegermany.terraplusminus.Terraplusminus"

    apiVersion = "1.21"

    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    authors = listOf("meysster", "Nudlsupp", "Nachwahl", "Zoriot")

    prefix = "T+-"

    loader = "de.btegermany.terraplusminus.PluginLibrariesLoader"
    generateLibrariesJson = true

    permissions {
        register("t+-.admin") {
            description = "Grants all Terraplusminus permissions"
            default = BukkitPluginDescription.Permission.Default.OP
            children = mapOf(
                "t+-.tpll" to true,
                "t+-.forcetpll" to true,
                "t+-.where" to true,
                "t+-.offset" to true,
                "t+-.distortion" to true,
                "t+-.notify.update" to true
            )
        }
        register("t+-.tpll") {
            description = "Allows usage of /tpll"
            default = BukkitPluginDescription.Permission.Default.TRUE
            children = mapOf(
                "t+-.tpll.ungenerated-chunks" to true
            )
        }
        register("t+-.tpll.ungenerated-chunks") {
            description = "Allows teleporting to chunks that have not been generated yet"
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("t+-.tpll.otherWorld") {
            description = "Allows /tpll to resolve a T+- world when the player is in a non-T+- world"
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("t+-.forcetpll") {
            description = "Allows force-teleporting other players with /tpll -p"
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("t+-.where") {
            description = "Allows usage of /where"
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("t+-.offset") {
            description = "Allows usage of /offset"
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("t+-.distortion") {
            description = "Allows usage of /distortion"
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("t+-.autoteleport") {
            description = "Exempts a player from automatic cross-world teleportation on height boundary crossing"
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
        register("t+-.notify.update") {
            description = "Notifies the player about available plugin updates"
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}

tasks {
    generatePaperPluginDescription {
        useDefaultCentralProxy()
    }
}

tasks.jar {
    archiveClassifier = "UNSHADED"
    enabled = false
}

tasks.shadowJar {
    archiveClassifier = ""
    relocate(
        "org.bstats",
        "de.btegermany.terraplusminus.libs.bstats"
    )
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}
