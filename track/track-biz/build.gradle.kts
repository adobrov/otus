//plugins {
//    id("build-kmp")
//}
//
//kotlin {
//    sourceSets {
//        all { languageSettings.optIn("kotlin.RequiresOptIn") }
//
//        commonMain {
//            dependencies {
//                implementation(kotlin("stdlib-common"))
//
//                //implementation(libs.cor)
//
//                implementation(project(":track-common"))
//                implementation(project(":track-stubs"))
//            }
//        }
//        commonTest {
//            dependencies {
//                implementation(kotlin("test-common"))
//                implementation(kotlin("test-annotations-common"))
//
//                api(libs.coroutines.test)
//                //implementation(projects.okMarketplaceRepoTests)
//                //implementation(projects.okMarketplaceRepoInmemory)
//            }
//        }
//        jvmMain {
//            dependencies {
//                implementation(kotlin("stdlib-jdk8"))
//            }
//        }
//        jvmTest {
//            dependencies {
//                implementation(kotlin("test-junit"))
//            }
//        }
//    }
//}

plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version


dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":track-common"))
    implementation(project(":track-stubs"))

    api(libs.kotlinx.datetime)
    api("ru.otus.otuskotlin.track.libs:track-lib-logging-common")
}