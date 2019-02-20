package com.carsecurity.web.rest.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Route(

        val id: Long,

        @JsonProperty("start_position_id")
        val startPositionId: Long,

        @JsonProperty("end_position_id")
        val endPositionId: Long,

        val length: Float,

        @JsonProperty("car_id")
        val carId: Long,
        val note: String
)