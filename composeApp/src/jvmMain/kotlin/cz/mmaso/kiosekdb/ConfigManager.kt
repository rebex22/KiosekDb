package cz.mmaso.kiosekdb

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class AppConfig(
    val ScannerUrl: String = "https://localhost:8080/",
    val ApiUrl: String = "https://localhost:8080/",
    val StartScannerUrl: String = "https://localhost:8090/api/v1/triggernewdocument",
    val StopScannerUrl: String = "https://localhost:8090/api/v1/cancelnewdocument",
)

object ConfigManager {

    private const val FILE_NAME = "build/config.json"

    // Nastavení JSON parseru (prettyPrint udělá JSON hezky čitelný pro člověka)
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true // Aplikace nespadne, když uživatel do JSONu přidá nesmysl
    }

    fun loadConfig(): AppConfig {
        val resourcesPath = System.getProperty("compose.application.resources.dir")
        if (resourcesPath != null) {
            val configFile = File(resourcesPath, "config.json")
            if (configFile.exists()) {
                val jsonString = configFile.readText()
                // println("Načteno z instalační složky: $jsonString")
            }
        }

        val file = File(resourcesPath )

        return if (file.exists()) {
            try {
                // Přečtení textu ze souboru a jeho naparsování
                val jsonString = file.readText()
                json.decodeFromString<AppConfig>(jsonString)
            } catch (e: Exception) {
                println("Chyba při čtení configu, používám výchozí: ${e.message}")
                AppConfig() // V případě poškozeného JSONu vrátíme výchozí
            }
        } else {
            // Soubor neexistuje, vytvoříme ho s výchozími daty
            val defaultConfig = AppConfig()
            saveConfig(defaultConfig)
            defaultConfig
        }
    }

    fun saveConfig(config: AppConfig) {
        val file = File(FILE_NAME)
        val jsonString = json.encodeToString(config)
        file.writeText(jsonString)
    }
}