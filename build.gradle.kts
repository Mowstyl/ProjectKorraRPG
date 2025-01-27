import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort

plugins {
    `java-library`
    alias(libs.plugins.shadowPlugin)
    alias(libs.plugins.generatePOMPlugin)
    alias(libs.plugins.spotBugsPlugin)
    //alias(libs.plugins.userdevPlugin)  // NMS
}

group = "com.projectkorra"
version = "1.1.4"
description = "Rpg side-plugin for ProjectKorra core"
base.archivesName = gradle.extra["projectName"].toString()

ext.set("projectName", gradle.extra["projectName"].toString())
maven.pom {
    name = gradle.extra["projectName"].toString()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.ORACLE
    }
}

repositories {
    gradlePluginPortal {
        content {
            includeGroup("com.gradleup")
            includeGroup("io.papermc.paperweight")
        }
    }
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
        content {
            includeGroup("io.papermc.paper")
            includeGroup("com.mojang")
            includeGroup("net.md-5")
        }
    }
    maven {
        url = uri("https://jitpack.io")
        content {
            includeGroupByRegex("com\\.github\\..*")
        }
    }
    mavenCentral()
    // mavenLocal()
}

dependencies {
    //paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")  // NMS
    compileOnly(libs.papermc.paperapi)
    compileOnly(libs.projectkorra.core)
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filesMatching("**/plugin.yml") {
            expand( project.properties )
        }
    }

    shadowJar {

    }

    spotbugsMain {
        reports.create("html") {
            required = true
            outputLocation = file("${layout.buildDirectory.get()}/reports/spotbugs.html")
            setStylesheet("fancy-hist.xsl")
        }
    }
}

spotbugs {
    ignoreFailures = false
    showStackTraces = true
    showProgress = true
    effort = Effort.DEFAULT
    reportLevel = Confidence.DEFAULT
}

// 1)
// For >=1.20.5 when you don't care about supporting spigot
//paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

// 2)
// For 1.20.4 or below, or when you care about supporting Spigot on >=1.20.5
// Configure reobfJar to run when invoking the build task
//tasks.assemble {
//    dependsOn(tasks.reobfJar)
//}

// Configure plugin.yml generation
// - name, version, and description are inherited from the Gradle project.
/*
bukkitPluginYaml {
    main = "io.papermc.paperweight.testplugin.TestPlugin"
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP
    authors.add("Author")
    apiVersion = "1.21"
}
*/