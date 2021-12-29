import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.cadixdev.gradle.licenser.LicenseExtension
import org.ajoberstar.grgit.Grgit

plugins {
    java
   `java-library`

    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.cadixdev.licenser") version "0.6.1"
    id("org.ajoberstar.grgit") version "4.1.1"

    idea
    eclipse
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
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.17-R0.1-SNAPSHOT")
    compileOnly("com.mojang:authlib:1.5.25")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit:1.17-419")
    implementation("net.lingala.zip4j:zip4j:2.9.1")
    implementation("dev.notmyfault.serverlib:ServerLib:2.3.1")
    implementation("org.bstats:bstats-bukkit:2.2.1")
    implementation("org.bstats:bstats-base:2.2.1")
    implementation("io.papermc:paperlib:1.0.7")
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
            include(dependency("dev.notmyfault.serverlib:ServerLib:2.3.1"))
        }
        relocate("org.bstats", "com.arcaniax.gobrush.metrics") {
            include(dependency("org.bstats:bstats-base"))
            include(dependency("org.bstats:bstats-bukkit"))
        }
        relocate("io.papermc.lib", "com.arcaniax.gobrush.paperlib") {
            include(dependency("io.papermc:paperlib:1.0.7"))
        }
    }
    minimize()
}

tasks.named("build").configure {
    dependsOn("shadowJar")
}
