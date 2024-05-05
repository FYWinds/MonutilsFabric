repositories {
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.isxander.dev/releases") {
        name = "Xander Maven"
    }
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

plugins {
    id("fabric-loom") version "1.5-SNAPSHOT"
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    id("pl.allegro.tech.build.axion-release") version "1.17.0"
    kotlin("plugin.serialization") version "1.9.21"
}

base {
    val archivesBaseName: String by project
    archivesName.set(archivesBaseName)
}

scmVersion {
    versionCreator { version, _ ->
        "$minecraftVersion-$version"
    }
    versionIncrementer("incrementPatch")
    ignoreUncommittedChanges = false
    scmVersion.checks.aheadOfRemote = false
    scmVersion.checks.snapshotDependencies = false
}

val minecraftVersion: String by project
val mavenGroup: String by project
group = mavenGroup
project.version = scmVersion.version

loom {
    splitEnvironmentSourceSets()

    mods {
        create("monutils") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("client"))
        }
    }

}

val yarnMappings: String by project
val loaderVersion: String by project
val fabricVersion: String by project
val fabricKotlinVersion: String by project
val yaclVersion: String by project
val kxSerializationVersion: String by project

configurations.shadow {
    isTransitive = false
}

dependencies {
    // To change the versions, see the gradle.properties file
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")
    // Uncomment the following line to enable the deprecated Fabric API modules.
    // These are included in the Fabric API production distribution and allow you to update your mod to the latest modules at a later more convenient time.

    // modImplementation "net.fabricmc.fabric-api:fabric-api-deprecated:${project.fabric_version}"

    modImplementation("dev.isxander.yacl:yet-another-config-lib-fabric:$yaclVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kxSerializationVersion")
}

tasks {
    processResources {
        filesMatching("fabric.mod.json") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        from("LICENSE")
    }


    compileKotlin {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.freeCompilerArgs = listOf("-Xno-source-debug-extension")
    }

    java {
        withSourcesJar()

        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    build {
        doLast {
            println("Moving old files to archive.")
            println("Keeping ${remapJar.get().archiveFileName.get()}.")

            val libsDir = layout.buildDirectory.dir("libs")
            val archiveDir = layout.buildDirectory.dir("libs/archive")
            if (!archiveDir.get().asFile.exists()) {
                archiveDir.get().asFile.mkdirs()
            }
            copy {
                from(libsDir)
                include("*.jar")
                into(archiveDir)
            }
            delete(fileTree(libsDir).matching {
                include("*.jar")
                exclude(remapJar.get().archiveFileName.get())
            })

            val devLibsDir = layout.buildDirectory.dir("devlibs")
            val devlibsArchiveDir = layout.buildDirectory.dir("devlibs/archive")
            if (!devlibsArchiveDir.get().asFile.exists()) {
                devlibsArchiveDir.get().asFile.mkdirs()
            }
            copy {
                from(devLibsDir)
                include("*.jar")
                exclude(jar.get().archiveFileName.get())
                into(devlibsArchiveDir)
            }
            delete(fileTree(devLibsDir).matching {
                include("*.jar")
                exclude(jar.get().archiveFileName.get())
            })
        }
    }

}
