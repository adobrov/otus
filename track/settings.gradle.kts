rootProject.name = "track"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":track-api-v1")
include(":track-api-v1-mappers")

include(":track-api-log")
//include(":track-libs")
include(":track-app-ktor")

include(":track-common")
include(":track-app-common")
include(":track-repo-common")
include(":track-repo-inmemory")
include(":track-repo-stubs")
include(":track-repo-test")
include(":track-repo-postgres")
include(":track-biz")
include(":track-stubs")


