package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Car

/**
 * Service which is used for sending requests to data server.
 */
interface CarService {

    /**
     * Method send request to data server to get all users cars. Result is returned as an array of cars.
     *
     * @return array of returned cars.
     */
    fun getCars(): Array<Car>

    /**
     * Method send request to data server to create a new car. Result is returned as a string.
     *
     * @param name is name of new car.
     * @param note is note about new car.
     * @return string as a response from server.
     */
    fun createCar(name: String, note: String): String

    /**
     * Method send request to data server to update existing car.
     *
     * @param id is identification of car in database on data server.
     * @param name is new car name.
     * @param note is new car note.
     */
    fun updateCar(id: Long, name: String, note: String)

    /**
     * Method send request to data server to remove car specified by [carId].
     *
     * @param carId is identification of car which will be deleted in datase on data server.
     */
    fun removeCar(carId: Long)

    /**
     * Method remove all cars of logged user on data server.
     */
    fun removeCars()
}