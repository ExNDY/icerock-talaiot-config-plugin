/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("jvm") version ("1.9.22")
    kotlin("plugin.serialization") version ("1.9.22")
    id("com.gradle.plugin-publish") version ("1.2.1")
    id("com.github.gmazzo.buildconfig") version ("5.3.5")
    id("java-gradle-plugin")
    id("org.gradle.maven-publish")
}

group = "dev.icerock.gradle"
version = "3.0.4"

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(gradleApi())
    implementation("io.github.cdsap.talaiot:talaiot:1.5.0") {
        exclude("io.github.cdsap.talaiot", "talaiot-request")
    }
    implementation("io.github.cdsap.talaiot.plugin:influxdb:1.5.0") {
        exclude("io.github.cdsap.talaiot", "talaiot-request")
    }
    implementation("com.influxdb:influxdb-client-kotlin:2.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("com.gradle:gradle-enterprise-gradle-plugin:3.16.1")
}

buildConfig {
    sourceSets.getByName("main") {
        packageName("dev.icerock.gradle.talaiot")
        buildConfig {
            buildConfigField("String", "influxUrl", "\"${properties["influx.url"]}\"")
            buildConfigField("String", "influxOrg", "\"${properties["influx.org"]}\"")
            buildConfigField("String", "influxBucket", "\"${properties["influx.bucket"]}\"")
            buildConfigField("String", "influxToken", "\"${properties["influx.token"]}\"")
            buildConfigField("String", "slackWebHook", "\"${properties["slack.webhook"]}\"")
        }
    }
}

gradlePlugin {
    plugins {
        create("icerock-talaiot") {
            id = "dev.icerock.gradle.talaiot"
            implementationClass = "dev.icerock.gradle.talaiot.TalaiotConfigPlugin"

            displayName = "Talaiot Wrapper for IceRock projects"
            description =
                "Wrapper for https://github.com/cdsap/Talaiot with predefined configuration for IceRock projects analytics"
            tags.set(listOf("talaiot"))
        }
    }

    website.set("https://github.com/icerockdev/icerock-talaiot-config-plugin")
    vcsUrl.set("https://github.com/icerockdev/icerock-talaiot-config-plugin")
}
