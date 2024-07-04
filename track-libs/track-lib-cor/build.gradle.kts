plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.test)
    testImplementation(kotlin("test-junit"))
    api(libs.kotlinx.datetime)
}