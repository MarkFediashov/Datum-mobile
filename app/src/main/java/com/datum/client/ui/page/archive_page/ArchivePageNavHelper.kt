package com.datum.client.ui.page.archive_page

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.*
import com.datum.client.dto.PathDto
import com.datum.client.repository.ArgumentRepository
import com.datum.client.ui.Page
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class ArchivePageNavHelper: NavHelper(){

    private val LINK_ID = "link"

    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf(
            navArgument(LINK_ID){
                type = NavType.IntType
            }
        )
    }

    override fun templateUrl(): String {
        return "archive-page/{$LINK_ID}"
    }

    override fun substituteArgument(vararg arr: Any): String {
        val argId = ArgumentRepository.putArgument(arr[0])
        return "archive-page/$argId"
    }

    override fun typeOf(): KClass<*> {
        return ArchivePage::class
    }

    fun getPath(backStack: NavBackStackEntry): PathDto{
        val id = getArg<Int>(backStack, LINK_ID)!!
        return ArgumentRepository.getArgument(id)
    }

}