package com.carsecurity.web.rest.service.Impl

import com.carsecurity.web.rest.model.Car
import com.carsecurity.web.rest.service.CarService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * Implementation of service which is used for sending requests to data server.
 *
 * @param restServerUrl is url address of data server
 * @param restTemplate is template which can be used for sending authorized requests to data server.
 */
@Service
class CarServiceImpl(

        @Value("\${rest.server.url}")
        private val restServerUrl: String,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : CarService {

    /**
     * Method send request to data server to get all users cars. Result is returned as an array of cars.
     *
     * @return array of returned cars.
     */
    override fun getCars(): Array<Car> {

        val url = "$restServerUrl$CAR_MAPPING"

        val routeEntity = restTemplate.getForEntity(url, Array<Car>::class.java)
        return routeEntity.body!!
    }

    /**
     * Method send request to data server to create a new car. Result is returned as a string.
     *
     * @param name is name of new car.
     * @param note is note about new car.
     * @return string as a response from server.
     */
    override fun createCar(name: String, note: String): String {
        val url = "$restServerUrl$CAR_MAPPING"

        val form = HashMap<String, String>()
        form["name"] = name
        form["note"] = note

        val httpEntity = HttpEntity(form)

        val entity = restTemplate.postForEntity(url, httpEntity, String::class.java)
        return entity.body!!
    }

    /**
     * Method send request to data server to update existing car.
     *
     * @param id is identification of car in database on data server.
     * @param name is new car name.
     * @param note is new car note.
     */
    override fun updateCar(id: Long, name: String, note: String) {
        val url = "$restServerUrl$CAR_MAPPING"

        val form = HashMap<String, String>()
        form["name"] = name
        form["note"] = note
        form["id"] = id.toString()

        val httpEntity = HttpEntity(form)

        restTemplate.put(url, httpEntity)
    }

    /**
     * Method send request to data server to remove car specified by [carId].
     *
     * @param carId is identification of car which will be deleted in datase on data server.
     */
    override fun removeCar(carId: Long) {
        val url = "$restServerUrl$CAR_MAPPING?car_id=$carId"

        restTemplate.delete(url)
    }

    /**
     * Method remove all cars of logged user on data server.
     */
    override fun removeCars() {
        val url = "$restServerUrl$CAR_MAPPING"
        restTemplate.delete(url)
    }
}