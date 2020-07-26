import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":protocol"))
    implementation(project(":utils"))
    implementation(kotlin("stdlib-jdk8"))

    compileOnly(Deps.BUNGEECORD)

    testImplementation(Deps.BUNGEECORD)
    testImplementation(Deps.MOCKITO_KOTLIN)
}

docker {
    name = "bungeecord"
}

repositories {
    maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}