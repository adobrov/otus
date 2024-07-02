//plugins {
//    id("build-kmp")
//}
//
//kotlin {
//    sourceSets {
//        val coroutinesVersion: String by project
//        commonMain {
//            dependencies {
//                implementation(kotlin("stdlib-jdk8"))
//                implementation(libs.coroutines.core)
//
//                implementation(project(":track-common"))
//                implementation(project(":track-api-log"))
//                implementation(project(":track-biz"))
//                //implementation(projects.okMarketplaceBiz)
//            }
//        }
//        commonTest {
//            dependencies {
//                implementation(kotlin("test-common"))
//                implementation(kotlin("test-annotations-common"))
//
//                implementation(libs.coroutines.test)
//
//                implementation(project(":track-api-v1"))
//            }
//        }
//
//        jvmMain {
//            dependencies {
//                implementation(kotlin("stdlib-jdk8"))
//                implementation(libs.coroutines.core)
//
//                implementation(project(":track-common"))
//                implementation(project(":track-api-log"))
//            }
//        }
//
//        jvmTest {
//            dependencies {
//                implementation(kotlin("test"))
//            }
//        }
//        nativeTest {
//            dependencies {
//                implementation(kotlin("test"))
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

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.test)

    implementation(project(":track-common"))
    implementation(project(":track-api-log"))
    implementation(project(":track-biz"))
    implementation(project(":track-api-v1"))
    implementation(project(":track-api-v1-mappers"))


    implementation(kotlin("test-common"))
    implementation(kotlin("test-annotations-common"))
    testImplementation(kotlin("test-junit"))
}