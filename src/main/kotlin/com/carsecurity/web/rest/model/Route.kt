package com.carsecurity.web.rest.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Data class which represents routes which are transfer between this application and data server.
 */
data class Route(

        /** Identification number of route in database on data server. */
        val id: Long,

        /** Length of route in meters. */
        val length: Float,

        /** Average speed of car on route in km/h */
        @JsonProperty("avg_speed")
        val avgSpeed: Float,

        /** Seconds of which all route take */
        @JsonProperty("seconds_of_travel")
        val secondsOfTravel: Long,

        /** Time when route was created. In seconds sice epoch. */
        @JsonProperty("time")
        val timeEpochSeconds: Long,

        /** Identification number of car which create this route. */
        @JsonProperty("car_id")
        val carId: Long,

        /** Name of car which create this record. */
        @JsonProperty("car_name")
        val carName: String,

        /** Note about this route. */
        val note: String
)