package com.carsecurity.web.rest.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Data class which represents events which are transfer between this application and data server.
 */
data class Event(

        /** Identification number of event in database on data server. */
        val id: Long,

        /** Name of event type of which is this event. */
        @JsonProperty("event_type_name")
        val eventTypeName: String,

        /** Time when event was created. In seconds since epoch. */
        val time: Long,

        /** Name of car which creates this event. */
        @JsonProperty("car_name")
        val carName: String,

        /** Note about this event. */
        val note: String
)