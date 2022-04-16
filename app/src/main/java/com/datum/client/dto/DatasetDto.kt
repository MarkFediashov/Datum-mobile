package com.datum.client.dto

data class DatasetDto(val name: String?, val description: String?, val trainPercent: Double?, val validationPercent: Double?, val testPercent: Double?){
    fun isFilled(): Boolean{
        return name != null && trainPercent != null
    }
}
