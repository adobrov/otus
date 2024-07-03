plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":track-lib-logging-common"))
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.logback)

    implementation(libs.logback.logstash)
    api(libs.logback.appenders)
    api(libs.logger.fluentd)

    testImplementation(kotlin("test-junit"))
}

//plugins {
//    id("build-kmp")
//}
//
//kotlin {
//    sourceSets {
//        val commonMain by getting {
//            dependencies {
//                implementation(kotlin("stdlib-common"))
//                implementation(libs.kotlinx.datetime)
//                implementation(project(":track-lib-logging-common"))
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
//                implementation(kotlin("stdlib-jdk8"))
//                implementation(project(":track-lib-logging-common"))
//                implementation(libs.coroutines.core)
//                implementation(libs.kotlinx.datetime)
//                implementation(libs.logback)
//
//                implementation(libs.logback.logstash)
//                api(libs.logback.appenders)
//                api(libs.logger.fluentd)
//            }
//        }
//        val jvmTest by getting {
//            dependencies {
//                implementation(kotlin("test-junit"))
//            }
//        }
//    }
//}