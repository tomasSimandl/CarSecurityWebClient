package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Car


interface CarService {

    fun getCars(): Array<Car>
    fun createCar(name: String, note: String): String
    fun updateCar(id: Long, name: String, note: String)
    fun removeCar(carId: Long)
    fun removeCars()
}