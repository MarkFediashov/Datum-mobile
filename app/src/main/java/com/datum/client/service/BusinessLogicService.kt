package com.datum.client.service

import android.content.Context
import com.datum.client.dto.DatasetImageClass
import com.datum.client.dto.LoginCredentialsDto
import com.datum.client.dto.RefreshTokenDto
import com.datum.client.dto.TokenPairDto
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

    suspend fun uploadImage(id: Int, image: ByteArray) = apiService.putSample(id, image)
}

