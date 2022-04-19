package com.datum.client.ui.page.maintainer

import androidx.navigation.NamedNavArgument
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class MaintainerPageNavHelper: NavHelper() {
    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf()
    }

    override fun templateUrl(): String {
        return "user-page"
    }

    override fun substituteArgument(vararg arr: Any): String {
        return templateUrl()
    }

    override fun typeOf(): KClass<*> = MaintainerPage::class
}