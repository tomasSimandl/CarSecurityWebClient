package com.carsecurity.web.rest.service


interface ToolService {

    fun deactivateTool(carId: Long, tool: String): String?
    fun activateTool(carId: Long, tool: String): String?
}