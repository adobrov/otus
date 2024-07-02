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

//plugins {
//    id("build-kmp")
//}
//
//group = rootProject.group
//version = rootProject.version
//
//kotlin {
//    sourceSets {
//        val commonMain by getting {
//            dependencies {
//                implementation(kotlin("stdlib-common"))
//
//                api(libs.kotlinx.datetime)
//                api("ru.otus.otuskotlin.track.libs:track-lib-logging-common")
//            }
//        }
//        val commonTest by getting {
//            dependencies {
//                implementation(kotlin("test-common"))
//                implementation(kotlin("test-annotations-common"))
//            }
//        }
//    }
//}