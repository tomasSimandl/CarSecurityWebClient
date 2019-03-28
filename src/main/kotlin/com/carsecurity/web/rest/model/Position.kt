package com.carsecurity.web.rest.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Data class which represents positions which are transfer between this application and data server.
 */
data class Position(

        /** Identification number of position in database on data server. */
        val id: Long,

        /** Identification number of route in data server database on which was created this position. */
        @JsonProperty("route_id")
        val routeId: Long?,

        /** Latitude of record */
        val latitude: Float,

        /** Longitude of record */
        val longitude: Float,

        /** Altitude of record */
        val altitude: Float,

        /** Time when record was created. */
        val time: String,

        /** Sensor accuracy of record */
        val accuracy: Float,

        /** Actual speed when record was created */
        val speed: Float,

        /** Distance from last record */
        val distance: Float
)