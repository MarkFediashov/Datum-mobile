package com.datum.client.service.api

import com.datum.client.dto.*

interface DatumApiService {

    fun setDomain(domain: String)

    suspend fun login(credentialsDto: LoginCredentialsDto): TokenPairDto
    suspend fun refresh(refreshTokenDto: RefreshTokenDto): TokenPairDto
    suspend fun logout(): SuccessResultDto
    suspend fun checkDomain(domain: String): Boolean

    suspend fun setImageClasses(datasetMetadata: List<DatasetImageClassDto>)
    suspend fun deleteMetadata()
    suspend fun putSample(imageClassId: Int, image: ByteArray): SuccessResultDto?
    suspend fun getAllSamples(): List<DataSampleDto>
    suspend fun getImageClasses(): List<DatasetImageClass>
    suspend fun generateDataset(percent: Int): PathDto

    suspend fun getUserList(): List<UserDto>
    suspend fun deleteUser(id: Int): SuccessResultDto
    suspend fun createUser(creationDto: UserCreationDto): UserInvitationDto?
}