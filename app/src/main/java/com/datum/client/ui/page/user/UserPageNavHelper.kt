package com.datum.client.ui.page.user

import androidx.navigation.NamedNavArgument
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class UserPageNavHelper: NavHelper() {
    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf()
    }

    override fun templateUrl(): String = "user"

    override fun substituteArgument(vararg arr: Any): String = templateUrl()

    override fun typeOf(): KClass<*> {
        TODO("Not yet implemented")
    }
}