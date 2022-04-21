package com.datum.client.dto

data class DatasetDto<T>(val name: String?, val description: String?, val trainPercentile: Double?, val validationPercentile: Double?, val testPercentile: Double?, val imageClasses: List<T>){
    fun isFilled(): Boolean{
        return name != null || trainPercentile != null
    }
}
