package com.datum.client.ui.page.send_form

import android.graphics.Bitmap
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.datum.client.repository.ArgumentRepository
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class SendFormNavHelper: NavHelper() {

    companion object {
        private const val PHOTO_ID_ARG = "id"
    }

    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf(
            navArgument(PHOTO_ID_ARG){
                type = NavType.IntType
            })
    }

    override fun templateUrl(): String {
        return "send-form/{$PHOTO_ID_ARG}"
    }

    override fun substituteArgument(vararg arr: Any): String {
        val id = arr[0] as Int
        return "send-form/$id"
    }

    fun getPhoto(backStackEntry: NavBackStackEntry): Bitmap {
        val id = getArg<Int>(backStackEntry, PHOTO_ID_ARG)!!
        return ArgumentRepository.getArgument(id)
    }

    override fun typeOf(): KClass<SendForm> = SendForm::class

}