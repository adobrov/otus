plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":track-api-v1"))
    implementation(project(":track-common"))

    testImplementation(kotlin("test-junit"))
}