package com.carsecurity.web.rest.service


interface ToolService {

    fun activateTool(carId: Long): String
    fun deactivateTool(carId: Long): String
}