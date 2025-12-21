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

rootProject.name = "Face Filters"

include(":app")
include(":core_editor")
include(":feature_canvas")
include(":feature_face_detection")
include(":core_ui")
include(":feature_text")
include(":feature_image_editing")
include(":feature_stickers")
include(":feature_saving")
include(":feature_drawing")
