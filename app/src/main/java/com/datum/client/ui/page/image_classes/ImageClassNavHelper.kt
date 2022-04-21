package com.datum.client.ui.page.image_classes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class ImageClassNavHelper: NavHelper() {

    private val ID = "id"

    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf(
            navArgument(ID) {
                type = NavType.IntType
                nullable = true
                defaultValue = null
            }
        )
    }

    override fun templateUrl(): String {
        return "image-classes?$ID={$ID}"
    }

    override fun substituteArgument(vararg arr: Any): String {
        val id = arr[0] as Int?
        return "image-classes?$ID=$id"
    }

    override fun typeOf(): KClass<*> {
        return ImageClassPage::class
    }
}