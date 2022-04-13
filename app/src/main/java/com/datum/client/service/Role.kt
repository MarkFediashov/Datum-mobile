package com.datum.client.service

object Role {
    fun isUser(role: String) = role == "user"
    fun isMaintainer(role: String) = role == "maintainer"
}