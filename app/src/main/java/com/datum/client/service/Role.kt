package com.datum.client.service

object Role {
    fun isUser(role: String) = role == "user"
    fun isMaintainer(role: String) = role == "maintainer"

    fun nameOfRole(roleId: Int): String{
        return mapOf(1 to "User", 2 to "Maintainer")[roleId] ?: "Unknown"
    }
}