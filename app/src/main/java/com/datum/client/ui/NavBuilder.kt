package com.datum.client.ui.page

import androidx.navigation.*
import kotlin.reflect.KClass

abstract class NavHelper {
    abstract fun getNavArguments(): List<NamedNavArgument>
    abstract fun templateUrl(): String
    abstract fun substituteArgument(vararg arr: Any): String

    fun <T> getArg(backStackEntry: NavBackStackEntry, key: String): T? {
        return backStackEntry.arguments!![key] as T?
    }

    abstract fun typeOf(): KClass<*>
}