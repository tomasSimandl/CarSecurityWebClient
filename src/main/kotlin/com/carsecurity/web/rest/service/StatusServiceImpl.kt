package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Status
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
class StatusServiceImpl(

        @Value("\${rest.server.url}")
        private val restServerUrl: String,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : StatusService {

    /**
     * Method send request to get status from device to data server.
     *
     * @param carId is identification number of car to which should be send status request.
     * @return [Status] from device.
     */
    override fun getStatus(carId: Long): Status {

        val url = "$restServerUrl$STATUS_MAPPING?car_id=$carId"

        val eventEntity = restTemplate.getForEntity(url, Status::class.java)
        return eventEntity.body!!
    }
}