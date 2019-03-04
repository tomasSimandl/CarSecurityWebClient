package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Status



interface StatusService {

    fun getStatus(carId: Long): Status
}