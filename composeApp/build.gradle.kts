import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
    id("com.github.ben-manes.versions") version "0.53.0" // Zkontrolujte nejnovější verzi
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.androidx.navigation.composee)
            implementation(libs.androidx.material.navigation)

            implementation(libs.compose.material.icons)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.composeIcons.featherIcons)

            implementation(libs.logback.classic)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)

            implementation(libs.exposed.core)
            implementation(libs.exposed.jdbc)
            implementation(libs.h2)

            // implementation("com.microsoft.sqlserver:mssql-jdbc:13.2.1.jre11")
            // implementation("org.xerial:sqlite-jdbc:3.50.2.0")
            implementation("org.jetbrains.exposed:exposed-kotlin-datetime:1.2.0")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
            implementation("io.ktor:ktor-server-core:3.4.2")
            implementation("io.ktor:ktor-server-cio:3.4.2")
            // implementation("io.ktor:ktor-server-netty:3.4.2")
            implementation("io.ktor:ktor-server-content-negotiation:3.4.2")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.2")
            implementation("io.ktor:ktor-network-tls-certificates:3.4.2")
            implementation("io.ktor:ktor-server-netty:3.4.2")
            // implementation("io.ktor:ktor-server-routing:3.4.2")

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.websockets)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.okhttp)

            implementation("org.jetbrains.compose.components:components-resources:1.10.3") // Toto je klíčové
            implementation("org.jetbrains.compose.runtime:runtime:1.10.3")

            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")

            // implementation("jakarta.xml.ws:jakarta.xml.ws-api:4.0.3")
            // implementation("com.sun.xml.ws:jaxws-rt:4.0.3")
        }
    }
}


compose.desktop {
    application {
        mainClass = "cz.mmaso.kiosekdb.MainKt"
        jvmArgs("--enable-native-access=ALL-UNNAMED")

        nativeDistributions {
            appResourcesRootDir.set(project.layout.projectDirectory.dir("app_resources"))
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            modules(
                "java.sql",
                "java.naming",
                "java.management",
                "java.security.jgss",
                "jdk.security.auth",
                "java.xml"
            )

            windows {
                console = false
            }

            packageName = "cz.mmaso.kiosekdb"
            packageVersion = "1.0.0"
        }
    }
}
