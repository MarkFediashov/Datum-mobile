package com.datum.client.repository

import android.content.Context
import android.content.SharedPreferences
import com.datum.client.dto.LoginCredentialsDto
import com.datum.client.dto.RefreshTokenDto
import com.google.gson.Gson

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    //спользуем данный класс чтобы все данные хранились единообразно как объект,
    //даже если мы хотим сохранить всего лишь одно поле
    inner class SingleFieldWrapper<T>(var field:T)

    companion object Key {
        const val SHARED_PREFERENCE_NAME = "SETTINGS"
        const val REFRESH_TOKEN = "refresh_token_k"
        const val CREDENTIALS = "credentials_k"
        const val ENDPOINT = "endpoint_k"
    }

    val gson = Gson()

    private val preference = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    private inline fun <reified T> getByKey(key: String): T?{
        val strData = preference.getString(key, null)
        return if(strData == null) {
            null
        } else {
            gson.fromJson(strData, T::class.java)
        }
    }

    private inline fun <reified T> setByKey(key: String, obj: T){
        if (obj == null){
            preference.edit().remove(key).apply()
        } else {
            val strData = gson.toJson(obj)
            preference.edit().putString(key, strData).apply()
        }
    }


    override var refreshToken: RefreshTokenDto?
        get() = getByKey(REFRESH_TOKEN)
        set(value) = setByKey(REFRESH_TOKEN, value)
    override var credentials: LoginCredentialsDto?
        get() = getByKey(CREDENTIALS)
        set(value) = setByKey(CREDENTIALS, value)
    override var endpoint: String?
        get() = getByKey<SingleFieldWrapper<String>>(ENDPOINT)?.field
        set(value) = setByKey(ENDPOINT, SingleFieldWrapper(value))
}