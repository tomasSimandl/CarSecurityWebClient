package com.carsecurity.web.rest.model

/**
 * Data class which represents cars which are transfer between this application and data server.
 */
data class Car(
        /** Identification number of car in database on data server. */
        val id: Long,
        /** Username of user which is owner of this car. */
        val username: String,
        /** Number of routes which was created by this car. */
        val routes: Long,
        /** Number of event which was created by this car. */
        val events: Long,
        /** Name of car. */
        val name: String,
        /** Note about car. */
        val note: String
)