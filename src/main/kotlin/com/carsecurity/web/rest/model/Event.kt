package com.carsecurity.web.rest.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Event(

        val id: Long,

        @JsonProperty("event_type_name")
        val eventTypeName: String,

        val time: Long, // epoch seconds

        @JsonProperty("car_name")
        val carName: String,

        val note: String
)