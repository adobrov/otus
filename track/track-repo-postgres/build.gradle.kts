import com.bmuschko.gradle.docker.tasks.container.*
import com.bmuschko.gradle.docker.tasks.image.DockerPullImage
import com.github.dockerjava.api.command.InspectContainerResponse
import com.github.dockerjava.api.model.ExposedPort
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest
import java.util.concurrent.atomic.AtomicBoolean

plugins {
    id("build-jvm")
//    id("build-pgContainer")
    alias(libs.plugins.muschko.remote)
    alias(libs.plugins.liquibase)
}
repositories {
    google()
    mavenCentral()
}


dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":track-common"))
    api(project(":track-repo-common"))
    implementation(libs.bundles.exposed)
    liquibaseRuntime(libs.liquibase.core)
    liquibaseRuntime(libs.liquibase.picocli)
    liquibaseRuntime(libs.liquibase.snakeyml)
    api(libs.kotlinx.datetime)
    liquibaseRuntime(libs.db.postgres)
    implementation(kotlin("test-common"))
    implementation(kotlin("test-annotations-common"))
    implementation(libs.db.postgres)
    implementation("io.github.moreirasantos:pgkn:1.2.0")
    testImplementation(kotlin("test-junit"))
    testImplementation(project(":track-repo-test"))
}

var pgPort = 5432
val taskGroup = "pgContainer"
val pgDbName = "track"
val pgUsername = "trackUser"
val pgPassword = "123456"
val containerStarted = AtomicBoolean(false)

tasks {
    // Здесь в тасках запускаем PotgreSQL в контейнере
    // Накатываем liquibase миграцию
    // Передаем настройки в среду тестирования
    val postgresImage = "postgres:latest"
    val pullImage by creating(DockerPullImage::class) {
        group = taskGroup
        image.set(postgresImage)
    }
    val dbContainer by creating(DockerCreateContainer::class) {
        group = taskGroup
        dependsOn(pullImage)
        targetImageId(pullImage.image)
        withEnvVar("POSTGRES_PASSWORD", pgPassword)
        withEnvVar("POSTGRES_USER", pgUsername)
        withEnvVar("POSTGRES_DB", pgDbName)
        healthCheck.cmd("pg_isready")
        hostConfig.portBindings.set(listOf("5432:5432"))
        exposePorts("tcp", listOf(5432))
        hostConfig.autoRemove.set(true)
    }
    val stopPg by creating(DockerStopContainer::class) {
        group = taskGroup
        targetContainerId(dbContainer.containerId)
    }
    val startPg by creating(DockerStartContainer::class) {
        group = taskGroup
        dependsOn(dbContainer)
        targetContainerId(dbContainer.containerId)
        finalizedBy(stopPg)
    }
    val inspectPg by creating(DockerInspectContainer::class) {
        group = taskGroup
        dependsOn(startPg)
        finalizedBy(stopPg)
        targetContainerId(dbContainer.containerId)
        onNext(
            object : Action<InspectContainerResponse> {
                override fun execute(container: InspectContainerResponse) {
                    pgPort = container.networkSettings.ports.bindings[ExposedPort.tcp(5432)]
                        ?.first()
                        ?.hostPortSpec
                        ?.toIntOrNull()
                        ?: throw Exception("Postgres port is not found in container")
                }
            }
        )
    }
    val liquibaseUpdate = getByName("update") {
        group = taskGroup
        dependsOn(inspectPg)
        finalizedBy(stopPg)
        println(layout.projectDirectory.dir("migrations"))
        doFirst {
            println("waiting for a while ${System.currentTimeMillis()/1000000}")
            Thread.sleep(30000)
            println("LQB: \"jdbc:postgresql://localhost:$pgPort/$pgDbName\" ${System.currentTimeMillis()/1000000}")
            liquibase {
                activities {
                    register("main") {
                        arguments = mapOf(
                            "logLevel" to "info",
                            "searchPath" to layout.projectDirectory.dir("migrations").asFile.toString(),
                            "changelogFile" to "changelog-v0.0.1.sql",
                            "url" to "jdbc:postgresql://localhost:$pgPort/$pgDbName",
                            "username" to pgUsername,
                            "password" to pgPassword,
                            "driver" to "org.postgresql.Driver"
                        )
                    }
                }
            }
        }
    }
    val waitPg by creating(DockerWaitContainer::class) {
        group = taskGroup
        dependsOn(inspectPg)
        dependsOn(liquibaseUpdate)
        containerId.set(startPg.containerId)
        finalizedBy(stopPg)
        doFirst {
            println("PORT: $pgPort")
        }
    }
    withType(KotlinNativeTest::class).configureEach {
        dependsOn(liquibaseUpdate)
        finalizedBy(stopPg)
        doFirst {
            environment("postgresPort", pgPort.toString())
        }
    }
    withType(Test::class).configureEach {
        dependsOn(liquibaseUpdate)
        finalizedBy(stopPg)
        doFirst {
            environment("postgresPort", pgPort.toString())
        }
    }
}