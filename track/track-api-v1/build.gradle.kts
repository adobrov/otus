//import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
//
//plugins {
//    id("build-kmp")
//    alias(libs.plugins.crowdproj.generator)
//    alias(libs.plugins.kotlinx.serialization)
//}
//
//crowdprojGenerate {
//    packageName.set("${project.group}.api.v1")
//    inputSpec.set(rootProject.ext["spec-v1"] as String)
//}
//
//kotlin {
//    sourceSets {
//        val serializationVersion: String by project
//        val commonMain by getting {
//            kotlin.srcDirs(layout.buildDirectory.dir("generate-resources/src/commonMain/kotlin"))
//            dependencies {
//                implementation(kotlin("stdlib-common"))
//
//                implementation(libs.kotlinx.serialization.core)
//                implementation(libs.kotlinx.serialization.json)
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
//        val jvmTest by getting {
//            dependencies {
//                implementation(kotlin("test-junit"))
//            }
//        }
//    }
//}
//
//tasks {
//    val openApiGenerateTask: GenerateTask = getByName("openApiGenerate", GenerateTask::class) {
//        outputDir.set(layout.buildDirectory.file("generate-resources").get().toString())
//        finalizedBy("compileCommonMainKotlinMetadata")
//    }
//    filter { it.name.startsWith("compile") }.forEach {
//        it.dependsOn(openApiGenerateTask)
//    }
//}

plugins {
    id("build-jvm")
    alias(libs.plugins.openapi.generator)
}

sourceSets {
    main {
        java.srcDir(layout.buildDirectory.dir("generate-resources/main/src/main/kotlin"))
    }
}


openApiGenerate {
    val openapiGroup = "${rootProject.group}.api.v1"
    generatorName.set("kotlin")
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.api")
    modelPackage.set("$openapiGroup.models")
    invokerPackage.set("$openapiGroup.invoker")
    inputSpec.set(rootProject.ext["spec-v1"] as String)

    /**
     * Здесь указываем, что нам нужны только модели, все остальное не нужно
     * https://openapi-generator.tech/docs/globals
     */
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    /**
     * Настройка дополнительных параметров из документации по генератору
     * https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
     */
    configOptions.set(
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "jackson",
            "collectionType" to "list"
        )
    )
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.datatype)
    testImplementation(kotlin("test-junit"))
    api(libs.kotlinx.datetime)
    api("ru.otus.otuskotlin.track:track-common")
}

tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
    }
}