package com.carsecurity.web.rest.model

/**
 * Data class represents message which only return one count number.
 */
data class Count(

        /** Number of requested items. */
        val count: Long
)