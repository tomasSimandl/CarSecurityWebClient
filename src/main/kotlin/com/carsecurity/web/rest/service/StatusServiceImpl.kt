package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Status
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class StatusServiceImpl(

        @Value("\${rest.server.url}")
        private val restServerUrl: String,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : StatusService {

    override fun getStatus(carId: Long): Status {

        val url = "$restServerUrl$STATUS_MAPPING?car_id=$carId"

        val eventEntity = restTemplate.getForEntity(url, Status::class.java)
        return eventEntity.body!!
    }
}