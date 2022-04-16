package com.datum.client.ui.page.dataset_meta

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.datum.client.dto.DatasetDto
import com.datum.client.repository.ArgumentRepository
import com.datum.client.ui.page.NavHelper
import kotlin.reflect.KClass

class DatasetMetaNavHelper: NavHelper() {
    private val ID_ARG = "id"
    override fun getNavArguments(): List<NamedNavArgument> {
        return listOf(navArgument(ID_ARG){
            type = NavType.IntType
        })
    }

    override fun templateUrl(): String {
        return "dataset/{$ID_ARG}"
    }

    override fun substituteArgument(vararg arr: Any): String {
        val datasetId = ArgumentRepository.putArgument(arr[0])
        return "dataset/${datasetId}"
    }

    override fun typeOf(): KClass<*> {
        return DatasetMetaPage::class
    }

    fun getDatasetMeta(b: NavBackStackEntry) : DatasetDto {
        val id = getArg<Int>(b, ID_ARG)
        return ArgumentRepository.getArgument(id!!)
    }
}