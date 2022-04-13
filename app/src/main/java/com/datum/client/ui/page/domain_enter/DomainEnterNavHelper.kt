package com.datum.client.ui.page.domain_enter

import androidx.navigation.NamedNavArgument
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class DomainEnterNavHelper: NavHelper() {
    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf()
    }

    override fun templateUrl(): String {
        return "domain-enter"
    }

    override fun substituteArgument(vararg arr: Any): String {
        return templateUrl()
    }

    override fun typeOf(): KClass<*> {
        TODO("Not yet implemented")
    }
}