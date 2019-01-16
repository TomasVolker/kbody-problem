import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.internal.os.OperatingSystem

plugins {
    application
    kotlin("jvm") version "1.3.11"
}

group = "tomasvolker"
version = "1.0"

repositories {
    mavenCentral()
    maven { url  = uri("https://jitpack.io") }
    maven { url = uri("https://dl.bintray.com/openrndr/openrndr/") }
}

val openrndrVersion = "0.3.30"

val openrndrOS = when (OperatingSystem.current()) {
    OperatingSystem.WINDOWS -> "windows"
    OperatingSystem.LINUX -> "linux-x64"
    OperatingSystem.MAC_OS -> "macos"
    else -> error("unsupported OS")
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")

    compile("org.openrndr:openrndr-core:$openrndrVersion")
    compile("org.openrndr:openrndr-extensions:$openrndrVersion")
    compile("com.github.edwinrndr:openrndr-dnky:2d84175c0f")
    compile("com.github.openrndr.orx:orx-mesh-generators:v0.0.14")

    runtime("org.openrndr:openrndr-gl3:$openrndrVersion")
    runtime("org.openrndr:openrndr-gl3-natives-$openrndrOS:$openrndrVersion")

}

application {
    
    mainClassName = "tomasvolker.kbodyproblem.ui.KBodyProblemKt"
    
    if (openrndrOS == "macos")
        applicationDefaultJvmArgs += "-XstartOnFirstThread"
    
}

