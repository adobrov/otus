plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.test)

    implementation(project(":track-common"))
    implementation(project(":track-stubs"))

    api(libs.kotlinx.datetime)
    api("ru.otus.otuskotlin.track.libs:track-lib-logging-common")
    api("ru.otus.otuskotlin.track.libs:track-lib-cor")

    testImplementation(kotlin("test-junit"))
}