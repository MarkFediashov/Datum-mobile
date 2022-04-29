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


    override fun getNavArguments(): List<NamedNavArgument> {
        return emptyList()
    }

    override fun templateUrl(): String {
        return "image-classes"
    }

    override fun substituteArgument(vararg arr: Any): String {
        return "image-classes"
    }

    override fun typeOf(): KClass<*> {
        return ImageClassPage::class
    }
}