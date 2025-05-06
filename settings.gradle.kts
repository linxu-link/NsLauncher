import org.gradle.internal.impldep.org.bouncycastle.oer.its.etsi102941.Url
import java.net.URI
import java.net.URL

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        flatDir {
            dirs("libs")
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

rootProject.name = "NsLauncher"
include(":app")
include(":lib_launcher_core")
//include(":hmi")
include(":toolkit")
