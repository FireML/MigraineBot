plugins {
    `java-library`
    alias(libs.plugins.shadow)
}

group = "uk.firedev"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_25

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jda) {
        exclude(module="opus-java")
    }
    implementation(libs.boostedyaml)
}

tasks {
    jar {
        enabled = false
    }
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        manifest {
            attributes["Main-Class"] = "uk.firedev.migrainebot.Main"
        }
        archiveBaseName.set("migrainebot")
        archiveVersion.set("")
        archiveClassifier.set("")

        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        relocate("net.dv8tion.jda", "uk.firedev.migrainebot.libs.jda")
        relocate("dev.dejvokep.boostedyaml", "uk.firedev.migrainebot.libs.boostedyaml")
    }
}