rootProject.name = "VersatiaExample-DI-OptionalPlugins"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        // The Versatia artifacts are consumed from the local Maven repository (`./gradlew publishToMavenLocal` in each checkout)
        mavenLocal { content { includeGroup("com.github.marcoral.versatia") } }
        mavenCentral()
        // Needed to resolve the Paper API that VersatiaAPI exports
        maven("https://repo.papermc.io/repository/maven-public/")
        // VaultAPI, compiled against only; the server provides Vault when it is installed
        maven("https://jitpack.io") { content { includeGroup("com.github.MilkBowl") } }
    }

    // Shared versions and the Versatia artifacts; versatiaVersion is set in gradle.properties
    versionCatalogs {
        create("libs") {
            from("com.github.marcoral.versatia:VersatiaCatalog:${providers.gradleProperty("versatiaVersion").get()}")
        }
    }
}

