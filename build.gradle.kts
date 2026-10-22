import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
   `java-library`

    id("com.gradleup.shadow") version "9.6.1"
    id("com.diffplug.spotless") version "8.10.4"

    idea
    eclipse
}

configurations.all {
    attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 25)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.compileJava.configure {
    options.release.set(8)
}

repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://libraries.minecraft.net/") }
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    implementation(platform("com.intellectualsites.bom:bom-newest:1.56"))
    compileOnly("io.papermc.paper:paper-api:26.3.build.+")
    compileOnly("net.md-5:bungeecord-api:1.21-R0.4")
    compileOnly("com.mojang:authlib:1.5.25")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit")
    implementation("net.lingala.zip4j:zip4j:2.11.6")
    implementation("org.bstats:bstats-bukkit:3.2.1")
    implementation("org.bstats:bstats-base:3.2.1")
}

version = "3.8.0-SNAPSHOT"

spotless {
    java {
        licenseHeaderFile(rootProject.file("HEADER.txt"))
        targetExclude("**/XMaterial.java")
        target("**/*.java")
    }
}

tasks.named<Copy>("processResources") {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set(null as String?)
    relocate("net.lingala.zip4j", "com.arcaniax.zip4j")
    relocate("org.bstats", "com.arcaniax.gobrush.metrics")
    dependencies {
        include(dependency("net.lingala.zip4j:zip4j"))
        include(dependency("org.bstats:bstats-base"))
        include(dependency("org.bstats:bstats-bukkit"))
    }
    minimize()
}

tasks.named("build").configure {
    dependsOn("shadowJar")
}
