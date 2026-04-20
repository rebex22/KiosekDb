package cz.mmaso.kiosekdb.api

import cz.mmaso.kiosekdb.Passport
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.parameters
import kotlinx.serialization.Serializable

private const val NETWORK_TIME_OUT = 30_000L
private const val API_KEY_1 = "063cdada-3a57-475a-9bc0-fab57a89c2f4";
private const val API_KEY_2 = "c4c80e40-d7d2-4161-9265-dc9d96736d81";

@Serializable
sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

@Serializable
data class PassportRequest(
    val passport: Passport
)

@Serializable
data class PassportResponse(
    val isValid: Boolean,
    val error: String?,
)

@Serializable
data class KodletuRequest(
    val passport: Passport,
    val kodLetu: String
)

@Serializable
data class KodletuResponse(
    val isValid: Boolean,
    val error: String?,
)

@Serializable
data class UctenkaRequest(
    val passport: Passport,
    val kodLetu: String
)

@Serializable
data class UctenkaResponse(
    val isValid: Boolean,
    val error: String?,
)

class KiosekApi( val httpClient: HttpClient, val apiBaseUrl: String ) {

    suspend fun kiosek_passport( req: PassportRequest ) : Result<PassportResponse> {
        try {
            val response = httpClient.post("$apiBaseUrl/passport" ) {
                headers {
                    append("api-key", "")
                }
                contentType(ContentType.Application.Json)
                setBody(PassportRequest)
            }
            if( response.status == HttpStatusCode.OK ) {

                // val dataS = response.body<String>()
                // println(dataS)

                val data = response.body<PassportResponse>()
                return Result.Success(data)
            }else {
                if( response.status == HttpStatusCode.Unauthorized ) {
                    return Result.Error(Exception("Invalid username or password"))
                }else {
                    return Result.Error(Exception("Http return code: ${response.status.value}"))
                }
            }
        } catch ( ex:Exception ) {
            return Result.Error( ex )
        }
    }

    suspend fun kiosek_kodletu( req: KodletuRequest ) : Result<KodletuResponse> {
        try {
            val response = httpClient.post("$apiBaseUrl/kodletu" ) {
                headers {
                    append("api-key", "")
                }
                contentType(ContentType.Application.Json)
                setBody(KodletuRequest)
            }
            if( response.status == HttpStatusCode.OK ) {

                // val dataS = response.body<String>()
                // println(dataS)

                val data = response.body<KodletuResponse>()
                return Result.Success(data)
            }else {
                if( response.status == HttpStatusCode.Unauthorized ) {
                    return Result.Error(Exception("Invalid username or password"))
                }else {
                    return Result.Error(Exception("Http return code: ${response.status.value}"))
                }
            }
        } catch ( ex:Exception ) {
            return Result.Error( ex )
        }
    }

    suspend fun kiosek_uctenka( req: UctenkaRequest ) : Result<UctenkaResponse> {
        try {
            val response = httpClient.post("$apiBaseUrl/uctenka" ) {
                headers {
                    append("api-key", "")
                }
                contentType(ContentType.Application.Json)
                setBody(UctenkaRequest)
            }
            if( response.status == HttpStatusCode.OK ) {

                // val dataS = response.body<String>()
                // println(dataS)

                val data = response.body<UctenkaResponse>()
                return Result.Success(data)
            }else {
                if( response.status == HttpStatusCode.Unauthorized ) {
                    return Result.Error(Exception("Invalid username or password"))
                }else {
                    return Result.Error(Exception("Http return code: ${response.status.value}"))
                }
            }
        } catch ( ex:Exception ) {
            return Result.Error( ex )
        }
    }


}