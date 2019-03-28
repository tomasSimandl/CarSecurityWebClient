package com.carsecurity.web.rest.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Data class which represents authorization token which is transfer between this application and authorization server
 * at user login or at token refresh.
 */
data class Token(

        /** Token used with actual user to access resources on all connected servers. */
        @JsonProperty("access_token")
        val accessToken: String,

        /** Type of used token. */
        @JsonProperty("token_type")
        val tokenType: String,

        /** Refresh token which is used for new access token when is expires. */
        @JsonProperty("refresh_token")
        val refreshToken: String = "",

        /** Number of seconds in witch expires sing request was received. */
        @JsonProperty("expires_in")
        val expiresIn: Long,

        /** Scope of user. */
        @JsonProperty("scope")
        val scope: String,

        /**
         * Is time when request was received.
         */
        @JsonIgnore
        val incomeTime: Long = Date().time
)