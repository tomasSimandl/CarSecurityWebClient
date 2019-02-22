package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Car
import com.carsecurity.web.rest.model.Route


interface CarService {

    fun getCars(): Array<Car>
    fun createCar(name: String, icon: String): String
}