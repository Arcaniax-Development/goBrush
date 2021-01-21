plugins {
    id("java")
    id("java-library")
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
    implementation("net.md-5:bungeecord-api:1.16-R0.4-SNAPSHOT")
    compileOnlyApi("com.mojang:authlib:1.5.25")
    compileOnlyApi("com.intellectualsites.fawe:FAWE-Bukkit:1.16-555")
}

version = "3.6.0"

tasks.named<Copy>("processResources") {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}
