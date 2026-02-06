plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.0"
    id("io.freefair.lombok") version "9.1.0"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("de.eldoria.plugin-yml.paper") version "0.8.0"
}

group = "net.astralechoes"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation("org.spongepowered:configurate-hocon:4.3.0-SNAPSHOT")

    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    runServer {
        minecraftVersion("1.21.8")
    }
    shadowJar {
        archiveClassifier.set("")
    }
}

paper {
    name = "ae-lobby"
    version = project.version.toString()
    main = "net.astralechoes.lobby.LobbyPlugin"
    description = "Lobby plugin for Astral Echoes"
    apiVersion = "1.21.8"
    website = "https://astralechoes.net"
    authors = listOf("SpektrSoyuz")
    foliaSupported = false
}