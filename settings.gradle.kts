pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "APPs"

include(":app")

// Core modules
include(":core:core-model")
include(":core:core-common")
include(":core:core-ui")
include(":core:core-network")
include(":core:core-database")

// Feature modules
include(":feature:feature-auth")
include(":feature:feature-feed")
include(":feature:feature-post")
include(":feature:feature-profile")
include(":feature:feature-search")
