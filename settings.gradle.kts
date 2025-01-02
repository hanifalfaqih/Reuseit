pluginManagement {
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

rootProject.name = "Reuseit"
include(":app")
include(":unityLibrary")
include(":unityLibrary:mobilenotifications.androidlib")
project(":unityLibrary:mobilenotifications.androidlib").projectDir = File(rootDir, "unityLibrary/mobilenotifications.androidlib")

