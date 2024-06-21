plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    implementation(kotlin("stdlib-jdk8"))

    api(libs.kotlinx.datetime)
    api("ru.otus.otuskotlin.track.libs:track-lib-logging-common")
}