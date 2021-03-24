import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.cadixdev.gradle.licenser.LicenseExtension

plugins {
    id("java")
    id("java-library")
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("org.cadixdev.licenser") version "0.5.1"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = sourceCompatibility
}

repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://repo.maven.apache.org/maven2") }
    maven { url = uri("https://libraries.minecraft.net/") }
    maven { url = uri("https://mvn.intellectualsites.com/content/repositories/releases/") }
    maven { url = uri("https://mvn.intellectualsites.com/content/repositories/thirdparty/") }
}

dependencies {
    compileOnlyApi("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.16-R0.4")
    compileOnlyApi("com.mojang:authlib:1.5.25")
    compileOnlyApi("com.intellectualsites.fawe:FAWE-Bukkit:1.16-583")
    implementation("net.lingala.zip4j:zip4j:2.7.0")
    implementation("de.notmyfault:serverlib:1.0.1")
    implementation("org.bstats:bstats-bukkit:2.2.1")
    implementation("org.bstats:bstats-base:2.2.1")
}

version = "3.7.2"

configure<LicenseExtension> {
    header = rootProject.file("HEADER")
    include("**/*.java")
    exclude("**/XMaterial.java")
    newLine = false
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
        relocate("de.notmyfault", "com.arcaniax.gobrush") {
            include(dependency("de.notmyfault:serverlib:1.0.1"))
        }
        relocate("org.bstats", "com.arcaniax.gobrush.metrics") {
            include(dependency("org.bstats:bstats-base"))
            include(dependency("org.bstats:bstats-bukkit"))
        }
    }
    minimize()
}

tasks.named("build").configure {
    dependsOn("shadowJar")
}

