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

    override suspend fun getDatasetMetadata(): DatasetDto<DatasetImageClass> {
        return networkClient.sendAndGetResponseBodyOfType(ApiPath.Dataset.GET_META, HttpMethod.Get, "")
    }

    override suspend fun setDatasetMetadata(datasetMetadata: DatasetDto<DatasetImageClassDto>) {
        return networkClient.sendAndGetResponseBodyOfType(ApiPath.Dataset.POST_META, HttpMethod.Post, datasetMetadata)
    }

    override suspend fun updateDatasetMetadata(meta: DatasetDto<DatasetImageClassDto>) {
        assert(meta.imageClasses.isNullOrEmpty())
        return networkClient.sendAndGetResponseBodyOfType(ApiPath.Dataset.UPDATE_META, HttpMethod.Post, meta)
    }

    override suspend fun putSample(imageClassId: Int, image: ByteArray): SuccessResultDto? {
        val parts = listOf(
            FormPart("ImageClassId", imageClassId),
            FormPart("Image", image, Headers.build {
                append(HttpHeaders.ContentType, "image/jpeg")
                append(HttpHeaders.ContentDisposition, "filename=file.jpeg")
            })
        )
        return try{
            val data = networkClient.sendMultipart(ApiPath.Dataset.ADD_SAMPLE, parts)
            Gson().fromJson(data, SuccessResultDto::class.java)
        } catch(_: Exception){
            null
        }
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
        return networkClient.sendAndGetResponseBodyOfType(ApiPath.User.LIST, HttpMethod.Get, "")
    }

    override suspend fun deleteUser(id: Int): SuccessResultDto {
        return networkClient.sendAndGetResponseBodyOfType(ApiPath.User.delete(id), HttpMethod.Post, "")
    }

    override suspend fun createUser(creationDto: UserCreationDto): UserInvitationDto? {
        return networkClient.sendAndGetResponseBodyOfType(ApiPath.User.ADD, HttpMethod.Post, creationDto)
    }
}