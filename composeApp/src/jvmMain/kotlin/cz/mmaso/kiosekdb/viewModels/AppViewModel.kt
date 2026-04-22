package cz.mmaso.kiosekdb.viewModels

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mmaso.kiosekdb.AppConfig
import cz.mmaso.kiosekdb.Passport
import cz.mmaso.kiosekdb.VIRTUAL_KEYS
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class ScannerResponse(
    val method: String,
    val status: String,
)

class AppViewModel( val config: AppConfig, val httpClient: HttpClient ) : ViewModel() {

    private var _showProgress = MutableStateFlow( false )
    val showProgress = _showProgress.asStateFlow()

    private var _passport = MutableStateFlow<Passport?>( null )
    val passport = _passport.asStateFlow()

    private var _scannerIsReady = MutableStateFlow<Boolean>( false )
    val scannerIsReady = _scannerIsReady.asStateFlow()

    private var _kodLetu = MutableStateFlow<TextFieldValue>(TextFieldValue(""))
    val kodLetu = _kodLetu.asStateFlow()

    fun showProgress(show: Boolean) {
        _showProgress.value = show
    }

    fun setPassport(passport: Passport) {
        _passport.value = passport
    }

    fun startScanner() {
        viewModelScope.launch(Dispatchers.IO ) {
            println("--- startScanner")
            try {
                val resp:ScannerResponse = httpClient.get(config.StartScannerUrl).body()
                if( resp.status == "OK" ) {
                    println("${resp.method} - ${resp.status}")
                    _scannerIsReady.value = true
                }
            }catch (e: Exception){
                println( e.message )
                _scannerIsReady.value = false
            }
        }
    }

    fun stopScanner(){
        viewModelScope.launch(Dispatchers.IO ) {
            println("--- stopScanner")
            try {
                val resp:ScannerResponse = httpClient.get(config.StopScannerUrl).body()
                if( resp.status == "OK" ) {
                    println("${resp.method} - ${resp.status}")
                    _scannerIsReady.value = false
                }
                /*
                val response = httpClient.get(config.StopScannerUrl)
                if (response.status == HttpStatusCode.OK) {
                    _scannerIsReady.value = false
                }*/
            }catch (e: Exception){
                println( e.message )
            }
        }
    }

    fun setKodeLetu( kl: String ) {
        _kodLetu.update {
            var s = kl
            if( s.length > 10 )
            {
                s = s.substring(0, 10)
            }
            TextFieldValue(
                text = s.uppercase(),
                selection = TextRange(kl.length)
            )
        }
    }

    fun processKb( str: String ) {
        _kodLetu.value?.let {
            var novyText = it.text + str
            if( novyText.length > 10 )
            {
                novyText = novyText.substring(0, 10)
            }
            _kodLetu.update {
                TextFieldValue(
                    text = novyText.uppercase(),
                    selection = TextRange(novyText.length)
                )
            }
        }
    }

    fun processKbCtrl( typ: VIRTUAL_KEYS) {
        when( typ ) {
            VIRTUAL_KEYS.KB_BACKSPACE -> {
                _kodLetu.update {
                    val novyText = it.text.dropLast(1 )
                    TextFieldValue(
                        text = novyText,
                        selection = TextRange(novyText.length)
                    )
                }
            }
            VIRTUAL_KEYS.KB_ENTER -> {

            }
            VIRTUAL_KEYS.KB_SPACE -> {
                _kodLetu.value.let {
                    if( it.text.length > 0 ) {
                        var novyText = it.text + " "
                        if( novyText.length > 10 )
                        {
                            novyText = novyText.substring(0, 10)
                        }
                        _kodLetu.update {
                            TextFieldValue(
                                text = novyText,
                                selection = TextRange(novyText.length)
                            )
                        }
                    }
                }
            }
        }
    }

    fun checkKodLetu( kodletu:String ) {
        viewModelScope.launch {
            _showProgress.update { true  }

            delay(2000 )

            _showProgress.update { false }
        }
    }

}