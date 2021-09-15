import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.cadixdev.gradle.licenser.LicenseExtension
import org.ajoberstar.grgit.Grgit

plugins {
    id("java")
    id("java-library")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("org.cadixdev.licenser") version "0.6.1"
    id("org.ajoberstar.grgit") version "4.1.0"
}

the<JavaPluginExtension>().toolchain {
    languageVersion.set(JavaLanguageVersion.of(16))
}

configurations.all {
    attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 16)
}

tasks.compileJava.configure {
    options.release.set(8)
}

repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://libraries.minecraft.net/") }
    maven { url = uri("https://mvn.intellectualsites.com/content/groups/public/") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnlyApi("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.17-R0.1-SNAPSHOT")
    compileOnlyApi("com.mojang:authlib:1.5.25")
    compileOnlyApi("com.fastasyncworldedit:FAWE-Bukkit:1.17-257")
    implementation("net.lingala.zip4j:zip4j:2.9.0")
    implementation("org.incendo.serverlib:ServerLib:2.2.1")
    implementation("org.bstats:bstats-bukkit:2.2.1")
    implementation("org.bstats:bstats-base:2.2.1")
    implementation("io.papermc:paperlib:1.0.6")
}

var buildNumber by extra("")
ext {
    val git: Grgit = Grgit.open {
        dir = File("$rootDir/.git")
    }
    val commit: String? = git.head().abbreviatedId
    buildNumber = if (project.hasProperty("buildnumber")) {
        project.properties["buildnumber"] as String
    } else {
        commit.toString()
    }
}

version = String.format("%s-%s", rootProject.version, buildNumber)

configure<LicenseExtension> {
    header.set(resources.text.fromFile(file("HEADER.txt")))
    include("**/*.java")
    exclude("**/XMaterial.java")
    newLine.set(false)
}

tasks.named<Copy>("processResources") {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set(null as String?)
    dependencies {
        relocate("net.lingala.zip4j", "com.arcaniax.zip4j") {
            include(dependency("net.lingala.zip4j:zip4j"))
        }
        relocate("org.incendo.serverlib", "com.arcaniax.gobrush.serverlib") {
            include(dependency("org.incendo.serverlib:ServerLib:2.2.1"))
        }
        relocate("org.bstats", "com.arcaniax.gobrush.metrics") {
            include(dependency("org.bstats:bstats-base"))
            include(dependency("org.bstats:bstats-bukkit"))
        }
        relocate("io.papermc.lib", "com.arcaniax.gobrush.paperlib") {
            include(dependency("io.papermc:paperlib:1.0.6"))
        }
    }
    minimize()
}

tasks.named("build").configure {
    dependsOn("shadowJar")
}

