package com.toptal.ggurgul.timezones.security.permissions

enum class Permission(
        val permissionName: String
) {

    READ_TIMEZONE("timezone:read"),
    EDIT_TIMEZONE("timezone:edit"),
    DELETE_TIMEZONE("timezone:delete");

    companion object {
        fun permissionOf(permissionName: String) = values().find { it.permissionName == permissionName }!!
    }

}