package com.carsecurity.web.rest.model

data class Car(
        val id: Long,
        val username: String,
        val routes: Long,
        val events: Long,
        val name: String,
        val note: String
)