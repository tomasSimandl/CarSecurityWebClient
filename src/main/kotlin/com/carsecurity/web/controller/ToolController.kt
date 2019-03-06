package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.ToolService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class ToolController(
        private val toolService: ToolService
) {

    private val logger = LoggerFactory.getLogger(ToolController::class.java)

    @PostMapping("tool/activate")
    @ResponseBody
    fun activateTool(
            model: Model,
            @RequestParam(value = "car_id") carId: Long
    ){
        toolService.activateTool(carId)
    }

    @PostMapping("tool/deactivate")
    @ResponseBody
    fun deactivateTool(
            model: Model,
            @RequestParam(value = "car_id") carId: Long
    ){
        toolService.deactivateTool(carId)
    }
}