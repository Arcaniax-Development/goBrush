import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("java-library")
    id("com.github.johnrengelman.shadow") version "6.1.0"
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
}

dependencies {
    compileOnlyApi("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    implementation("net.md-5:bungeecord-api:1.16-R0.4")
    compileOnlyApi("com.mojang:authlib:1.5.25")
    compileOnlyApi("com.intellectualsites.fawe:FAWE-Bukkit:1.16-555")
    implementation("net.lingala.zip4j:zip4j:2.6.4")
}

version = "3.6.0"

tasks.named<Copy>("processResources") {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set(null as String?)
    dependencies {
        include(dependency("net.lingala.zip4j:zip4j"))
    }
}

tasks.named("build").configure {
    dependsOn("shadowJar")
}

