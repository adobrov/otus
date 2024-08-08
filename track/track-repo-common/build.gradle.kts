plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":track-common"))
    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test-annotations-common"))
}