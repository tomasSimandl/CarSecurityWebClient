package com.carsecurity.web.rest.model

/**
 * Simplified representation of user which is used for transfer between this application and authorization server.
 */
data class User(

        /** Identification number of user in database on authorization server. */
        var id: Long = 0,

        /** Username of user. */
        var username: String = "",

        /** Users e-mail. */
        val email: String = ""
)