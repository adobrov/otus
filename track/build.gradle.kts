plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.muschko.remote) apply false
    alias(libs.plugins.muschko.java) apply false
}

group = "ru.otus.otuskotlin.track"
version = "0.0.1"

subprojects {
    group = rootProject.group
    version = rootProject.version
}

allprojects {
    repositories {
        mavenCentral()
    }
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("track-v1.yaml").toString())
    set("spec-log", specDir.file("track-log.yaml").toString())
}

tasks {
    arrayOf("build", "clean", "check").forEach {tsk ->
        create(tsk) {
            group = "build"
            dependsOn(subprojects.map {  it.getTasksByName(tsk,false)})
        }
    }
}