package com.datum.client.service.api

import com.datum.client.dto.*
import com.datum.client.service.BusinessLogicService
import com.datum.client.service.network.NetworkClient
import com.google.gson.Gson
import io.ktor.client.request.forms.*
import io.ktor.http.*
import java.util.logging.Level
import java.util.logging.Logger

class DatumApiServiceImpl : DatumApiService {
    private val networkClient: NetworkClient = NetworkClient { BusinessLogicService.instance.actualTokenPair?.accessToken }

    override fun setDomain(domain: String){
        networkClient.host = "https://$domain"
    }

    override suspend fun login(credentialsDto: LoginCredentialsDto): TokenPairDto {
        try {
            return networkClient.sendAndGetResponseBodyOfType<TokenPairDto>(ApiPath.Auth.LOGIN, HttpMethod.Post, credentialsDto)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun refresh(refreshTokenDto: RefreshTokenDto): TokenPairDto {
        try {
            return networkClient.sendAndGetResponseBodyOfType<TokenPairDto>(ApiPath.Auth.REFRESH, HttpMethod.Post, refreshTokenDto)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun logout(): SuccessResultDto {
        TODO("Not yet implemented")
    }

    private fun buildUrl(domain: String, path: String) = "https://$domain$path"

    override suspend fun checkDomain(domain: String): Boolean {
        val url = buildUrl(domain, ApiPath.Auth.CHECK)
        return try {
            val result = networkClient.send(url, HttpMethod.Get)
            result.status == HttpStatusCode.OK
        } catch(e: Exception){
            false
        }
    }

    override suspend fun getDatasetMetadata(): DatasetDto {
        return DatasetDto("Test dataset", "Test dataset for ...", trainPercent = 0.4, testPercent = 0.4, validationPercent = 0.2)
    }

    override suspend fun setDatasetMetadata(datasetMetadata: DatasetDto) {
        TODO("Not yet implemented")
    }

    override suspend fun putSample(imageClassId: Int, image: ByteArray): SuccessResultDto {
        val parts = listOf(
            FormPart("ImageClassId", imageClassId),
            FormPart("Image", image, Headers.build {
                append(HttpHeaders.ContentType, "image/jpeg")
                append(HttpHeaders.ContentDisposition, "filename=file.jpeg")
            })
        )
        val data = networkClient.sendMultipart(ApiPath.Dataset.ADD_SAMPLE, parts)
        Logger.getGlobal().log(Level.INFO, "123123123123 $data")
        return Gson().fromJson(data, SuccessResultDto::class.java)
    }

    override suspend fun getAllSamples(): List<DataSampleDto> {
        TODO("Not yet implemented")
    }

    /**
     * Raw implementation: should send request on backend
     **/
    override suspend fun getImageClasses(): List<DatasetImageClass> {
        return listOf(
            DatasetImageClass(0, "class1", "test1"),
            DatasetImageClass(1, "class2", "test2")
        )
    }

    override suspend fun getUserList(): List<UserDto> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(id: Int): SuccessResultDto {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(creationDto: UserCreationDto): UserInvitationDto {
        TODO("Not yet implemented")
    }
}