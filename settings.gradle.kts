rootProject.name = "MigraineBot"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("jda", "net.dv8tion:JDA:6.4.1")
            library("boostedyaml", "dev.dejvokep:boosted-yaml:1.3.7")

            plugin("shadow", "com.gradleup.shadow").version("9.4.1")
        }
    }
}