package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Position
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * Implementation of service which is used for sending requests to data server.
 *
 * @param restServerUrl is url address of data server
 * @param restTemplate is template which can be used for sending authorized requests to data server.
 */
@Service
class PositionServiceImpl(

        @Value("\${rest.server.url}")
        private val restServerUrl: String,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : PositionService {

    /**
     * Method send request to data server to get all positions of given route.
     *
     * @param routeId is identification number of route in database on data server.
     * @return array of received positions.
     */
    override fun getPositions(routeId: Long): Array<Position> {

        val url = "$restServerUrl$POSITION_MAPPING?route_id=$routeId&limit=${Int.MAX_VALUE}"

        val positionEntity = restTemplate.getForEntity(url, Array<Position>::class.java)
        return positionEntity.body!!
    }
}