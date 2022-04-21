package com.datum.client.dto

data class DatasetDto<T>(val name: String?, val description: String?, val trainPercent: Double?, val validationPercent: Double?, val testPercent: Double?, val imageClasses: List<T>){
    fun isFilled(): Boolean{
        return name != null || trainPercent != null
    }
}
