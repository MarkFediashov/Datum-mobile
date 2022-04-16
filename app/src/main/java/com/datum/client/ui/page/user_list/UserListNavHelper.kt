package com.datum.client.ui.page.user_list

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.datum.client.dto.UserDto
import com.datum.client.repository.ArgumentRepository
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class UserListNavHelper: NavHelper() {

    companion object{
        const val ID = "id"
    }

    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf(
            navArgument(ID) {
                type = NavType.IntType
            }
        )
    }

    override fun templateUrl(): String {
        return "user-list/{id}"
    }

    override fun substituteArgument(vararg arr: Any): String {
        val id = ArgumentRepository.putArgument(arr[0])
        return "user-list/$id"
    }

    fun getList(backStackEntry: NavBackStackEntry): List<UserDto> {
        val id = backStackEntry.arguments!![ID] as Int
        return ArgumentRepository.getArgument(id)
    }

    override fun typeOf(): KClass<*> {
        return UserListPage::class
    }
}