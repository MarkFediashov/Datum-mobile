package com.datum.client.service

import com.datum.client.ui.custom.DropdownMenuEntity

class Role private constructor(override val id: Int, override val name: String) : DropdownMenuEntity {

    companion object {

        private val USER = Role(1, "user")
        private val MAINTAINER = Role(2, "maintainer")
        private val ROLE_MAP: Map<Int, Role> =
            Role::class.java.declaredFields
            .filter { java.lang.reflect.Modifier.isStatic(it.modifiers) && it.type.equals(Role::class.java) }
            .map { it.get(null) as Role }
            .associateBy { it.id }

        fun isUser(role: String) = role == USER.name

        fun by(roleId: Int): Role {
            return ROLE_MAP[roleId] ?: throw Exception("Role not found")
        }
    }
}