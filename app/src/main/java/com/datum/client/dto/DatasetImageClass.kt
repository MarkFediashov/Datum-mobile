package com.datum.client.dto

import com.datum.client.ui.custom.DropdownMenuEntity

data class DatasetImageClass(override val id: Int, override val name: String, val description: String) : DropdownMenuEntity {

}