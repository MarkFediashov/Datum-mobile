package com.datum.client.service.network


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.util.logging.Level
import java.util.logging.Logger

class NetworkClient (val tokenProvider: () -> String?){

    val clientFactory by lazy { ClientFactory() }

    lateinit var host: String

    inner class ClientFactory {
        fun create(isJson: Boolean = true): HttpClient = HttpClient(CIO) {
            defaultRequest {
                if(isJson) {
                    contentType(ContentType.Application.Json)
                }

                val token = tokenProvider()
                if(token != null) {
                    header("Authorization", "Bearer $token")
                }
            }
            install(JsonFeature){
                serializer = GsonSerializer()
            }
        }
    }

    suspend fun sendRequestForString(url: String): String {
        val client = HttpClient(CIO)
        val r = client.get<String>(url)
        return r
    }

    suspend fun send(url: String, method: HttpMethod): HttpResponse {
        val client = HttpClient(CIO)
        return client.request(url) {
            this.method = method
        }
    }

    suspend inline fun <reified T> sendAndGetResponseBodyOfType(endpoint: String, method: HttpMethod, data: Any): T {
        val client = clientFactory.create()
        Logger.getGlobal().log(Level.INFO, "Реквест ${host + endpoint} с данными $data")
        return try {
            val response: HttpResponse = client.request(host + endpoint){
                this.method = method
                contentType(ContentType.Application.Json)
                body = data
            }

            val result: String = response.receive()
            Logger.getGlobal().log(Level.WARNING, "Ответ от $response ------ Данные: $result")

            client.close()
            if(response.status != HttpStatusCode.InternalServerError) response.receive() else throw Exception()

        } catch (e: ServerResponseException){
            Logger.getGlobal().log(Level.INFO, "Лол ошибка ${e.message}")
            throw e
        } catch (e: Exception){
            Logger.getGlobal().log(Level.INFO, "Лол ошибка ${e.message}")
            throw e
        }
    }

    suspend fun sendMultipart(endpoint: String, data: List<FormPart<*>>): String{
        val client = clientFactory.create(isJson=false)
        try {
            val response: HttpResponse = client.submitFormWithBinaryData(
                url = host + endpoint,
                formData = formData {
                    data.forEach { formPart ->
                        append(formPart)
                    }
                }
            )
            val data = response.readBytes().toString(Charsets.UTF_8)
            return data
        } catch (e:Exception){
            Logger.getGlobal().log(Level.INFO, "Лол ошибка ${e.message}")
            throw e
        }
    }



    suspend fun loadRaw(url: String): ByteArray {
        val client = clientFactory.create()

        val response = client.get<ByteArray>(Url(url)){
            contentType(ContentType.Any)
        }

        return response
    }
}