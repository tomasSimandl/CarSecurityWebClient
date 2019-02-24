package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Position
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class PositionServiceImpl(

        @Value("\${rest.server.url}")
        private val restServerUrl: String,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : PositionService {

    override fun getPositions(routeId: Long): Array<Position> {

        val url = "$restServerUrl$POSITION_MAPPING?route_id=$routeId&limit=${Int.MAX_VALUE}"

        val positionEntity = restTemplate.getForEntity(url, Array<Position>::class.java)
        return positionEntity.body!!
    }
}