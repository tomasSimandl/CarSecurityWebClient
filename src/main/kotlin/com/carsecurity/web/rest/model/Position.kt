package com.carsecurity.web.rest.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Position(

        val id: Long,
        @JsonProperty("route_id")
        val routeId: Long?,
        val latitude: Float,
        val longitude: Float,
        val altitude: Float,
        val time: String,
        val accuracy: Float,
        val speed: Float,
        val distance: Float
)