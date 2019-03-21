package com.carsecurity.web.rest.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Status(

        val battery: Float,

        @JsonProperty("is_charging")
        val isCharging: Boolean,

        @JsonProperty("is_power_save_mode")
        val powerSaveMode: Boolean,

        val tools: Map<String, Boolean>,

        val time: Long,

        @JsonProperty("car_id")
        val carId: Long
)