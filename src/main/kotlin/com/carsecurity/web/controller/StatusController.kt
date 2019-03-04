package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.StatusService
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class StatusController(
        private val statusService: StatusService
) {

    private val logger = LoggerFactory.getLogger(StatusController::class.java)

    @GetMapping("status")
    fun getStatus(
            model: Model,
            @RequestParam(value = "car_id") carId: Long
    ): String {

        val status = statusService.getStatus(carId)

        model.addAttribute("status", status)
        return "fragments/status :: statusFragment"
    }
}