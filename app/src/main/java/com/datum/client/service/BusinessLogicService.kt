package com.datum.client.service

import android.content.Context
import com.datum.client.dto.*
import com.datum.client.repository.SettingsRepository
import com.datum.client.repository.SettingsRepositoryImpl
import com.datum.client.service.api.DatumApiService
import com.datum.client.service.api.DatumApiServiceImpl

class BusinessLogicService(private val settingsRepository: SettingsRepository,
                           private val apiService: DatumApiService = DatumApiServiceImpl()
) {

    var actualTokenPair: TokenPairDto? = null

    private lateinit var imageClassesCache: List<DatasetImageClass>

    companion object {
        lateinit var instance: BusinessLogicService

        fun setup(context: Context){
            instance =  BusinessLogicService(SettingsRepositoryImpl(context))
            instance.apply {
                if(hasDomain()){
                    apiService.setDomain(settingsRepository.endpoint!!)
                }
            }
        }
    }

    fun getUserRole(): String = actualTokenPair!!.role

    fun hasDomain(): Boolean = settingsRepository.endpoint != null
    fun hasCredentials(): Boolean = settingsRepository.credentials != null
    fun hasRefreshToken(): Boolean = settingsRepository.refreshToken != null

    suspend fun checkDomain(domain: String): Boolean {
        val result = apiService.checkDomain(domain)
        if(result){
            apiService.setDomain(domain)
            settingsRepository.endpoint = domain
        }
        return result
    }

    suspend fun login(login: String, password: String): Boolean {
        val credentials = LoginCredentialsDto(login, password)
        return try {
            val tokenPair = apiService.login(credentials)
            imageClassesCache = apiService.getImageClasses()
            actualTokenPair = tokenPair
            settingsRepository.credentials = credentials
            settingsRepository.refreshToken = RefreshTokenDto(tokenPair.refreshToken)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun restoreSession(): Boolean{
        val refreshTokenDto = settingsRepository.refreshToken
        return if(refreshTokenDto!= null) {
            try {
                val tokenPair = apiService.refresh(refreshTokenDto)
                settingsRepository.refreshToken = RefreshTokenDto(tokenPair.refreshToken)
                actualTokenPair = tokenPair
                imageClassesCache = apiService.getImageClasses()
                true
            } catch (e: Exception) {
                settingsRepository.refreshToken = null
                false
            }
        } else {
            false
        }
    }

    fun getImageClasses() = imageClassesCache

    suspend fun uploadImage(id: Int, image: ByteArray) = apiService.putSample(id, image)?.success ?: false

    suspend fun getDatasetMeta() = apiService.getDatasetMetadata()

    suspend fun getUserList() = apiService.getUserList()

    suspend fun addUser(user: UserCreationDto): List<UserDto>? {
        return try {
            if(apiService.createUser(user) != null) {
                getUserList()
            }
            else null
        } catch (_: Exception) {
            return null
        }
    }
    suspend fun deleteUser(userId: Int): List<UserDto>? {
        return if(apiService.deleteUser(userId).success){
            getUserList()
        } else return null
    }

    fun deletePersistentData(){
        settingsRepository.apply {
            credentials = null
            endpoint = null
            refreshToken = null
        }
    }

    suspend fun setMetadata(meta: DatasetDto<DatasetImageClassDto>) = apiService.setDatasetMetadata(meta)
    suspend fun updateMetadata(meta: DatasetDto<DatasetImageClassDto>) = apiService.updateDatasetMetadata(meta)
    suspend fun deleteMetadata() = apiService.deleteMetadata()
}

