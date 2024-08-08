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
    implementation(project(":track-repo-common"))
    implementation(project(":track-stubs"))
    implementation(kotlin("test-junit"))
    implementation(kotlin("test-annotations-common"))
    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test-annotations-common"))
}