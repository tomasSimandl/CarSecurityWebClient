package com.carsecurity.web.rest.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Data class which represents status which is transfer between this application and data server.
 */
data class Status(

        /** Battery level status 0 = discharged, 1 = fully charged */
        val battery: Float,

        /** Indication if device is charging now */
        @JsonProperty("is_charging")
        val isCharging: Boolean,

        /** Indication if device is in power save mode */
        @JsonProperty("is_power_save_mode")
        val powerSaveMode: Boolean,

        /** Map with activated tools */
        val tools: Map<String, Boolean>,

        /** Time when status was created in milliseconds since epoch. */
        val time: Long,

        /** Car id of car which is associated with this status. */
        @JsonProperty("car_id")
        val carId: Long
)