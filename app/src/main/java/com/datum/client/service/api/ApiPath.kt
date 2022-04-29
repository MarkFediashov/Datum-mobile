package com.datum.client.service.api

object ApiPath {
    object Auth {
        val CHECK = "/api/auth/check"
        val LOGIN = "/api/auth/login"
        val REFRESH = "/api/auth/refresh"
        val LOGOUT = "/api/auth/logout"
    }

    object Dataset {
        val IMAGE_CLASSES = "/api/dataset/image-classes"
        val ADD_SAMPLE = "/api/dataset/add-sample"
        val LIST_SAMPLE = "/api/dataset/samples"
        fun archive(percent: Int): String = "/api/dataset/archive?percent=$percent"
        val CLEAR = "/api/dataset/clear"
    }

    object User {
        val LIST = "/api/user-control/list"
        fun delete(id: Int): String = "/api/user-control/delete/$id"
        val ADD = "/api/user-control/add"
    }
}