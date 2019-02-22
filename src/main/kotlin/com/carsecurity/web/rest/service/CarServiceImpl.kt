package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Car
import com.carsecurity.web.rest.model.Route
import com.carsecurity.web.rest.model.Token
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import javax.servlet.http.HttpSession


@Service
class CarServiceImpl(

        @Value("\${rest.server.url}")
        private val restServerUrl: String,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : CarService {

    override fun getCars(): Array<Car> {

        val url = "$restServerUrl$CAR_MAPPING"

        val routeEntity = restTemplate.getForEntity(url, Array<Car>::class.java)
        return routeEntity.body!!
    }

    override fun createCar(name: String, icon: String): String {
        val url = "$restServerUrl$CAR_MAPPING"

        val form = HashMap<String, String>()
        form["name"] = name
        form["icon"] = icon

        val httpEntity = HttpEntity(form)

        val entity = restTemplate.postForEntity(url, httpEntity, String::class.java)
        return entity.body!!
    }
}