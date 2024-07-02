//plugins {
//    id("build-kmp")
//}
//
//kotlin {
//    sourceSets {
//        val commonMain by getting {
//            dependencies {
//                implementation(kotlin("stdlib-common"))
//
//                implementation(project(":track-common"))
//            }
//        }
//        val commonTest by getting {
//            dependencies {
//                implementation(kotlin("test-common"))
//                implementation(kotlin("test-annotations-common"))
//            }
//        }
//        val jvmMain by getting {
//            dependencies {
//                implementation(kotlin("stdlib"))
//            }
//        }
//        val jvmTest by getting {
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
    implementation(kotlin("stdlib"))
    implementation(project(":track-api-v1"))
    implementation(project(":track-common"))

    testImplementation(kotlin("test-junit"))
}