rootProject.name = "MigraineBot"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("jda", "net.dv8tion:JDA:6.4.1")

            library("jshepherd-core", "de.bsommerfeld.jshepherd:core:4.1.0")
            library("jshepherd-yaml", "de.bsommerfeld.jshepherd:yaml:4.1.0")
            bundle("jshepherd", listOf("jshepherd-core", "jshepherd-yaml"))

            plugin("shadow", "com.gradleup.shadow").version("9.4.1")
        }
    }
}