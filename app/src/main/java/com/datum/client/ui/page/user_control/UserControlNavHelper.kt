package com.datum.client.ui.page.user_control

import androidx.navigation.NamedNavArgument
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class UserControlNavHelper: NavHelper() {
    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf()
    }

    override fun templateUrl(): String {
        return "user-control"
    }

    override fun substituteArgument(vararg arr: Any): String {
        return templateUrl()
    }

    override fun typeOf(): KClass<*> {
        return UserControlPage::class
    }
}