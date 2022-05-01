package com.datum.client.dto

import com.datum.client.types.NamedEntity

data class DatasetImageClassDto(override val name: String, val description: String) : NamedEntity
