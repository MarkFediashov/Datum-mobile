package com.datum.client.ui.page.maintainer

import androidx.navigation.NamedNavArgument
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class MaintainerPageNavHelper: NavHelper() {
    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf()
    }

    override fun templateUrl(): String = "maintainer"

    override fun substituteArgument(vararg arr: Any): String = templateUrl()

    override fun typeOf(): KClass<*> {
        TODO("Not yet implemented")
    }
}