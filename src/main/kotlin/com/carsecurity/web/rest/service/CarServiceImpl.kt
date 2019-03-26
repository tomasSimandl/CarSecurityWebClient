package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Car
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


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

    override fun createCar(name: String, note: String): String {
        val url = "$restServerUrl$CAR_MAPPING"

        val form = HashMap<String, String>()
        form["name"] = name
        form["note"] = note

        val httpEntity = HttpEntity(form)

        val entity = restTemplate.postForEntity(url, httpEntity, String::class.java)
        return entity.body!!
    }

    override fun updateCar(id: Long, name: String, note: String) {
        val url = "$restServerUrl$CAR_MAPPING"

        val form = HashMap<String, String>()
        form["name"] = name
        form["note"] = note
        form["id"] = id.toString()

        val httpEntity = HttpEntity(form)

        restTemplate.put(url, httpEntity)
    }

    override fun removeCar(carId: Long) {
        val url = "$restServerUrl$CAR_MAPPING?car_id=$carId"

        restTemplate.delete(url)
    }

    override fun removeCars() {
        val url = "$restServerUrl$CAR_MAPPING"
        restTemplate.delete(url)
    }
}