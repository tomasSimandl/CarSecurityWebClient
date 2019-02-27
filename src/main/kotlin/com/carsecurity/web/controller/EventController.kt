package com.carsecurity.web.controller

import com.carsecurity.web.rest.service.CarService
import com.carsecurity.web.rest.service.EventService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class EventController(
        private val eventService: EventService,
        private val carService: CarService
) {

    private val logger = LoggerFactory.getLogger(EventController::class.java)

    @GetMapping("event")
    fun getEvents(
            model: Model,
            @RequestParam(value = "car_id", required = false) carId: Long?
    ): String {

        val cars = carService.getCars()
        val events = if(carId == null){
            eventService.getEvents(page = 0)
        } else {
            eventService.getEventsByCar(page = 0, carId = carId)
        }

        model.addAttribute("events", events)
        model.addAttribute("cars", cars)
        return "event"
    }

    @GetMapping("event", params = ["page"])
    fun loadEvents(
            model: Model,
            @RequestParam(value = "page") page: Int
    ): String {
        val events = eventService.getEvents(page)

        model.addAttribute("events", events)
        return "fragments/event :: events"
    }

    @PutMapping("event")
    fun updateEvent(
            @RequestParam(value = "id") carId: Long,
            @RequestParam(value = "note") note: String
    ) {
        eventService.updateEvent(carId, note)
    }

    @DeleteMapping("event")
    fun deleteEvent(
            @RequestParam(value = "event_id") eventId: Long
    ) {
        eventService.removeEvent(eventId)
    }
}