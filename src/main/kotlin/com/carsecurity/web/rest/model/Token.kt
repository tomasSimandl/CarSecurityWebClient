package com.carsecurity.web.rest.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Token (

        @JsonProperty("access_token")
        val accessToken: String,

        @JsonProperty("token_type")
        val tokenType: String,

        @JsonProperty("refresh_token")
        val refreshToken: String = "",

        @JsonProperty("expires_in")
        val expiresIn: Long,

        @JsonProperty("scope")
        val scope: String,

        @JsonIgnore
        val incomeTime: Long = Date().time
)