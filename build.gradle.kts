import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("application")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.beryx.jlink") version "3.0.1"
    `maven-publish`
}

group = "br.com.saggion"
version = "1.2.0"
description = "A project that provides an interface to use some functionality of the tools scrcpy and adb."

javafx {
    version = "22"
    modules = listOf("javafx.controls", "javafx.fxml")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0-RC2")
    implementation("org.controlsfx:controlsfx:11.1.2")
    implementation("commons-io:commons-io:2.16.1")
}

application {
    mainModule.set("kotlin.scrcpytools")
    mainClass.set("br.com.saggion.scrcpytools.App")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

jlink {
    options = listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
    launcher {
        name = "Screen Copy Tools"
        noConsole = true
    }
    jpackage {
        jvmArgs.add("-splash:app/splash.png")
        installerOptions.addAll(
            listOf(
                "--description",
                project.description.toString(),
                "--app-version",
                version.toString(),
                "--copyright",
                "Copyrigtht 2024 Felipe Saggion",
                "--license-file",
                "LICENSE",
                "--vendor",
                "Felipe Saggion",
            ),
        )
        if (org.gradle.internal.os.OperatingSystem.current().isLinux) {
            icon = "$projectDir/src/main/resources/br/com/saggion/scrcpytools/icon.png"
            installerOptions.addAll(
                listOf(
                    "--linux-menu-group",
                    "Development",
                    "--linux-shortcut",
                    "--linux-deb-maintainer",
                    "felipe.saggion@gmail.com",
                    "--linux-rpm-license-type",
                    "Apache-2.0",
                    "--icon",
                    "src/main/resources/launcher.png",
                ),
            )
        }
        if (org.gradle.internal.os.OperatingSystem.current().isWindows) {
            icon = "$projectDir/src/main/resources/br/com/saggion/scrcpytools/icon.ico"
            installerOptions.addAll(
                listOf(
                    "--win-per-user-install",
                    "--win-dir-chooser",
                    "--win-menu",
                    "--win-shortcut",
                    "--icon",
                    "src/main/resources/br/com/saggion/scrcpytools/icon.ico",
                ),
            )
        }
    }
}

tasks.jpackageImage {
    doLast {
        copy {
            from("src/main/resources/br/com/saggion/scrcpytools")
            include("splash.png")
            into("build/jpackage/${jlink.launcherData.get().name}/app")
        }
    }
}
