package com.datum.client.ui.page.dataset_control

import androidx.navigation.NamedNavArgument
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class DatasetControlNavHelper: NavHelper() {
    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf()
    }

    override fun templateUrl(): String {
        return "dataset-control"
    }

    override fun substituteArgument(vararg arr: Any): String {
        return templateUrl()
    }

    override fun typeOf(): KClass<*> {
        return DatasetControlPage::class
    }
}