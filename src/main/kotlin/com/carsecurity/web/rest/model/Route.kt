package com.carsecurity.web.rest.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Route(

        val id: Long,

        val length: Float,

        @JsonProperty("avg_speed")
        val avgSpeed: Float,

        @JsonProperty("seconds_of_travel")
        val secondsOfTravel: Long,

        @JsonProperty("time")
        val timeEpochSeconds: Long,

        @JsonProperty("car_id")
        val carId: Long,

        @JsonProperty("car_name")
        val carName: String,

        val note: String
)