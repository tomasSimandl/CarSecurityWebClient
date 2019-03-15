package com.carsecurity.web.rest.model


data class User(
        var id: Long = 0,
        var username: String = "",
        val email: String = ""
)