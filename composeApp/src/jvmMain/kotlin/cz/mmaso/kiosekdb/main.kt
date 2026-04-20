package cz.mmaso.kiosekdb

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cz.mmaso.kiosekdb.db.Tasks
import cz.mmaso.kiosekdb.db.VatRefund
import cz.mmaso.kiosekdb.module
import cz.mmaso.kiosekdb.scanner.PassportScannerBroker
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.network.tls.certificates.buildKeyStore
import io.ktor.network.tls.certificates.saveToFile
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.serverConfig
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.sslConnector
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.websocket.WebSocketDeflateExtension.Companion.install
import jdk.internal.org.jline.utils.Colors.s
import kiosekdb.composeapp.generated.resources.DefaultPageTitle
import kiosekdb.composeapp.generated.resources.Res
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.exposed.v1.core.count
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import java.io.File
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Locale
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import javax.sound.sampled.AudioSystem
import kotlin.io.println
import kotlin.jvm.java
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation

@Serializable
data class TransactionData(
    val jsonBarcode: String?,
    val jsonBirthDate: String?,
    val jsonClientId: String?,
    val jsonDateTimeISO9601: String?,
    val jsonDocumentClassCode: String?,
    val jsonDocumentNumber: String?,
    val jsonExpirationDate: String?,
    val jsonGivenName: String?,
    val jsonIssuingStateCode: String?,
    val jsonNationalityCode: String?,
    val jsonPersonalNumber: String?,
    val jsonSex: String?,
    val jsonSurname: String?,
    val images: String?
)

@Serializable
data class Passport(
    val hostName: String?,
    val userId: String?,
    val transactionId: String?,
    val transactionData: TransactionData?
)

fun runDbSqlite() {
    Database.connect("jdbc:sqlite:/data/data.db", "org.sqlite.JDBC")
}

/*
fun runDbMsSql() {
    println("--")
    Database.connect(
        "jdbc:sqlserver://PRG-SQL14;databaseName=EVDPH_Novela",
        "com.microsoft.sqlserver.jdbc.SQLServerDriver",
        user = "Evdph",
        password = "evdph369@"
    )

    transaction {
        val vr = VatRefund.selectAll().toList()
        println("${vr.size} vatrefunds!")
    }

    println("OK")
}*/

fun runDb() {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
    transaction {
        val vr = VatRefund.selectAll().toList()
        println("${vr.size} vatrefunds!")

        /*
        SchemaUtils.create(Tasks)

        val taskId = Tasks.insert {
            it[title] = "Learn Exposed"
            it[description] = "Go through the Get started with Exposed tutorial"
        } get Tasks.id

        val secondTaskId = Tasks.insert {
            it[title] = "Read The Hobbit"
            it[description] = "Read the first two chapters of The Hobbit"
            it[isCompleted] = true
        } get Tasks.id

        println("Created new tasks with ids $taskId and $secondTaskId.")

        Tasks.select(Tasks.id.count(), Tasks.isCompleted).groupBy(Tasks.isCompleted).forEach {
            println("${it[Tasks.isCompleted]}: ${it[Tasks.id.count()]} ")
        }

        println("Remaining tasks: ${Tasks.selectAll().toList()}")

        */
    }
}

private const val NETWORK_TIME_OUT = 30_000L

class CustomCookiesStorage : CookiesStorage {
    private val cookies = mutableMapOf<String, List<Cookie>>()
    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        val list = cookies.get(requestUrl.host)
        list?.let {
            cookies[requestUrl.host] = it + cookie
            println("Added cookie: $cookie")
        } ?: run {
            cookies[requestUrl.host] = listOf(cookie)
            println("Added cookie: $cookie")
        }
    }

    override fun close() {}

    override suspend fun get(requestUrl: Url): List<Cookie> {
        println("Get cookie: $requestUrl")
        return cookies.get( requestUrl.host ) ?: mutableListOf<Cookie>()
    }
}

internal class AllCertsTrustManager : X509TrustManager {

    @Suppress("TrustAllX509TrustManager")
    override fun checkServerTrusted(
        chain: Array<X509Certificate>,
        authType: String
    ) {
        // no-op
    }

    @Suppress("TrustAllX509TrustManager")
    override fun checkClientTrusted(
        chain: Array<X509Certificate>,
        authType: String
    ) {
        // no-op
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
}

fun getHttpClient() : HttpClient {
    return HttpClient(OkHttp) {
        install(WebSockets) {
            pingIntervalMillis = 20_000
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
        engine {
            config {
                val trustAllCert = AllCertsTrustManager()
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, arrayOf(trustAllCert), SecureRandom())
                sslSocketFactory(sslContext.socketFactory, trustAllCert)

                hostnameVerifier { _, _ ->
                    true
                }
            }
        }
        install(ClientContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    useAlternativeNames = true
                    ignoreUnknownKeys = true
                    encodeDefaults = false
                }
            )
        }
        install(HttpCookies) {
            storage = CustomCookiesStorage()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = NETWORK_TIME_OUT
            connectTimeoutMillis = NETWORK_TIME_OUT
            socketTimeoutMillis  = NETWORK_TIME_OUT
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("Ktor response length: ${message.length} Bytes")
                }
            }
            level = LogLevel.ALL
        }
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(username = "user1@email.com", password = "123456")
                    // DigestAuthCredentials(username = "jetbrains", password = "foobar")
                }
                realm = "Access to the '/api' path"
            }
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Any)
        }
    }
}

fun main() = application {

    /*
    val ll = Locale.getDefault()
    val t0 = stringResource(Res.string.DefaultPageTitle)
    println("${t0}")

    // Locale.setDefault(Locale.ENGLISH)
    Locale.setDefault(Locale.forLanguageTag("en-US"))
    val ll0 = Locale.getDefault()
    val t1 = stringResource(Res.string.DefaultPageTitle)
    println("${t1}")

    Locale.setDefault(Locale.forLanguageTag("he-IL"))
    val ll1 = Locale.getDefault()
    val t2 = stringResource(Res.string.DefaultPageTitle)
    println("${t2}")

    Locale.setDefault(Locale.forLanguageTag("ko-KR"))
    val ll2 = Locale.getDefault()
    val t3 = stringResource(Res.string.DefaultPageTitle)
    println("${t3}")

    Locale.setDefault(Locale.forLanguageTag("zh-CN"))
    val ll3 = Locale.getDefault()
    val t4 = stringResource(Res.string.DefaultPageTitle)
    println("${t4}")

    Locale.setDefault(Locale.forLanguageTag("ru-RU"))
    val ll4 = Locale.getDefault()
    val t5 = stringResource(Res.string.DefaultPageTitle)
    println("${t5}")
    */

    /*
    GlobalScope.launch {
        convert("GlobalResources.cz.json", "data.cz.txt")
        convert("GlobalResources.en.json", "data.en.txt")
        convert("GlobalResources.he.json", "data.he.txt")
        convert("GlobalResources.ko.json", "data.ko.txt")
        convert("GlobalResources.ru.json", "data.ru.txt")
        convert("GlobalResources.zh.json", "data.zh.txt")
    }*/

    val windowIcon = remember { mutableStateOf(Icons.Filled.AccountBox)}
    val windowState = rememberWindowState( placement = WindowPlacement.Maximized ) //  .Fullscreen
    // val windowState = rememberWindowState( width = 1280.dp, height = 800.dp, position = WindowPosition(Alignment.Center)),
    // val httpClient = remember { getHttpClient() }

    // Implementuje jednoduchy server na praci s cteckou pasu
    val server = remember {

        embeddedServer(
            factory = Netty,
            applicationEnvironment {
                log = LoggerFactory.getLogger("ktor.application")
            },
            configure = {
                envConfig()
            },
            module = Application::module
        ) // .start(wait = true)

        /*
        val keyStoreFile = File("c:\\Data\\Projects\\Projects_Java\\KiosekDb\\keystore.jks")
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())

        // Otevření souboru a zadání hesla
        keyStoreFile.inputStream().use { stream ->
            keyStore.load(stream, "heslo123".toCharArray())
        }

        embeddedServer(CIO, port = 8080, host = "0.0.0.0" ) {
            install( ServerContentNegotiation) {
                json()
            }
            routing {
                get("/status") {
                    call.respondText("Periferie, jsem připraven!")
                }
                post("/DocumentScan_axd") {
                    val passport = call.receive<Passport>()

                    println("--- Passport received ${passport.name}")

                    PassportScannerBroker.addRequest(passport)

                    call.respond(HttpStatusCode.OK)
                }
            }
        }*/
    }

    LaunchedEffect( Unit ) {
        server.start(wait = false)
        println("Server bezi na portu 8080")
        // runDb()
        // runDbMsSql()
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Kiosek",
        state = windowState,
        alwaysOnTop = false,
        undecorated = false, // zmizi okna a taskbar
        onKeyEvent = {
            if (it.key == Key.Escape && it.type == KeyEventType.KeyDown) {
                exitApplication()
                true
            } else
                false
        },
        icon = rememberVectorPainter(windowIcon.value)
    ) {
        App()
    }
}

private fun ApplicationEngine.Configuration.envConfig() {

    /*
    val keyStoreFile = File("build/keystore1.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, "123456")
    */

    val resourcesPath = System.getProperty("compose.application.resources.dir")
    if (resourcesPath != null) {
        val keyStoreFile = File(resourcesPath, "keystore1.jks")
        if( keyStoreFile.exists()) {

            // val keyStoreFile = File("build/keystore1.jks")
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())

            // Otevření souboru a zadání hesla
            keyStoreFile.inputStream().use { stream ->
                keyStore.load(stream, "123456".toCharArray())
            }

            connector {
                port = 8080
            }
            sslConnector(
                keyStore = keyStore,
                keyAlias = "sampleAlias",
                keyStorePassword = { "123456".toCharArray() },
                privateKeyPassword = { "foobar".toCharArray() }) {
                port = 8443
                keyStorePath = keyStoreFile
            }
        }
    }
}

fun Application.module() {
    install( ServerContentNegotiation) {
        json()
    }
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        post("/DocumentScan_axd") {
            // val ss = call.receive<String>()
            // println(ss)
            try {
                val passport = call.receive<Passport>()
                println("--- Passport received ${passport}")
                PassportScannerBroker.addRequest(passport)
            }catch (e: Exception) {
                e.printStackTrace()
            }

            call.respond(HttpStatusCode.OK)
        }
    }
}

suspend fun playSound(resourcePath: String) {
    try {
        val array = Res.readBytes( resourcePath )
        val inputStream = array.inputStream()
        val audioStream = withContext(Dispatchers.IO) {
            AudioSystem.getAudioInputStream(inputStream)
        }

        val clip = AudioSystem.getClip()
        clip.open(audioStream)
        clip.start() // Spustí přehrávání
        clip.addLineListener { event ->
            if (event.type == javax.sound.sampled.LineEvent.Type.STOP) {
                clip.close()
            }
        }
    } catch (e: Exception) {
        println( e.message )
    }
}

@Serializable
data class Resource( val pair:Map<String, String> )

suspend fun convert( name: String, ofname: String ) {
    val sb = StringBuilder()
    try {
        val jsonRes = Res.readBytes("files/$name").decodeToString()
        val r = Json.decodeFromString<Map<String, String>>(jsonRes)
        r.keys.forEach {
            val v =  r.get(it)
            val s = "<string name=\"$it\">$v</string>"
            sb.appendLine( s )
        }
        File("c:\\Data\\$ofname").writeText(sb.toString())
    }catch (e:Exception){
        println(e.message)
    }
}