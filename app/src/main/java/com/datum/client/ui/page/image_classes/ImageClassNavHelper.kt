package com.datum.client.ui.page.image_classes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.datum.client.dto.DatasetImageClass
import com.datum.client.dto.DatasetImageClassDto
import com.datum.client.repository.ArgumentRepository
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class ImageClassNavHelper: NavHelper() {

    companion object {
        const val EXISTED_CLASSES_ARG = "existed-classes"
    }

    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf(
            navArgument(EXISTED_CLASSES_ARG){
                type = NavType.IntType
            }
        )
    }

    override fun templateUrl(): String {
        return "image-classes/{$EXISTED_CLASSES_ARG}"
    }

    override fun substituteArgument(vararg arr: Any): String {
        val id = ArgumentRepository.putArgument(arr[0] as List<DatasetImageClass>)
        return "image-classes/${id}"
    }

    fun getExistedClasses(backStackEntry: NavBackStackEntry): List<DatasetImageClass> {
        val id = backStackEntry.arguments!![EXISTED_CLASSES_ARG] as Int
        return ArgumentRepository.getArgument(id)
    }

    override fun typeOf(): KClass<*> {
        return ImageClassPage::class
    }
}