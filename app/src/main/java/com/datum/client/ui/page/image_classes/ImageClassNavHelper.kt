package com.datum.client.ui.page.image_classes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.datum.client.dto.DatasetImageClassDto
import com.datum.client.repository.ArgumentRepository
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class ImageClassNavHelper: NavHelper() {

    private val ID = "id"

    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf(
            navArgument(ID) {
                type = NavType.IntType
                nullable = false
                defaultValue = -1
            }
        )
    }

    override fun templateUrl(): String {
        return "image-classes?$ID={$ID}"
    }

    override fun substituteArgument(vararg arr: Any): String {
        val id = ArgumentRepository.putArgument(arr[0] as List<DatasetImageClassDto>)
        return "image-classes?$ID=$id"
    }

    override fun typeOf(): KClass<*> {
        return ImageClassPage::class
    }

    fun getList(b: NavBackStackEntry): List<DatasetImageClassDto>? {
        return ArgumentRepository.getArgument(getArg<Int>(b, ID) ?: -1)
    }
}