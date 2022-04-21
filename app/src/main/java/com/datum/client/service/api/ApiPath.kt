package com.datum.client.service.api

object ApiPath {
    object Auth {
        val CHECK = "/api/auth/check"
        val LOGIN = "/api/auth/login"
        val REFRESH = "/api/auth/refresh"
        val LOGOUT = "/api/auth/logout"
    }

    object Dataset {
        val GET_META = "/api/dataset/meta"
        val POST_META = "/api/dataset/meta"
        val ADD_SAMPLE = "/api/dataset/add-sample"
        val LIST_SAMPLE = "/api/dataset/samples"
        val UPDATE_META = "/api/dataset/update-meta"
        val DELETE_META = "/api/dataset/delete-meta"
    }

    object User {
        val LIST = "/api/user-control/list"
        fun delete(id: Int): String = "/api/user-control/delete/$id"
        val ADD = "/api/user-control/add"
    }
}