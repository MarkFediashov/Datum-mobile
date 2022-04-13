package com.datum.client.ui.page.login

import androidx.navigation.NamedNavArgument
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class LoginPageNavHelper: NavHelper() {
    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf()
    }

    override fun templateUrl(): String = "login"

    override fun substituteArgument(vararg arr: Any): String = "login"

    override fun typeOf(): KClass<LoginPage> = LoginPage::class
}