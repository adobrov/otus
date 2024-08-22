plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.coroutines.core)
    implementation(libs.db.cache4k)
    implementation(libs.uuid)
    api(project(":track-common"))
    api(project(":track-repo-common"))
    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(project(":track-repo-test"))
}