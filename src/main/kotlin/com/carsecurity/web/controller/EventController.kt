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

/**
 * This controller is used for all requests which are associated with 'event' page.
 *
 * @param carService is service which is used for access cars on data server.
 * @param eventService is service which is used for access events on data server.
 */
@Controller
class EventController(
        private val eventService: EventService,
        private val carService: CarService
) {

    /** Logger of this class. */
    private val logger = LoggerFactory.getLogger(EventController::class.java)

    /**
     * Method send request to data server over [eventService] to get events and request over [carService] to get
     * all users cars. Results are append to [model]. Result is render on 'event' page.
     *
     * @param model is holder for page attributes.
     * @return string "event" which is rendered to event page.
     */
    @GetMapping("event")
    fun getEvents(
            model: Model,
            @RequestParam(value = "car_id", required = false) carId: Long?
    ): String {

        val loadNextUrl: String
        val events = if (carId == null) {
            loadNextUrl = "/event?"
            eventService.getEvents(page = 0)
        } else {
            loadNextUrl = "/event?car_id=$carId&"
            eventService.getEventsByCar(page = 0, carId = carId)
        }
        val cars = carService.getCars()

        model.addAttribute("events", events)
        model.addAttribute("cars", cars)
        model.addAttribute("loadNextUrl", "${loadNextUrl}page=")
        return "event"
    }

    /**
     * Method send request to data server over [eventService] to get events which are append to [model]. Result is
     * render as a fragment page 'events'.
     *
     * @param model is holder for page attributes.
     * @param page is requested page of events (Optional).
     * @param carId optional parameter which specified that events will be only of this car.
     * @return string which represents fragment of events which is render to panel of events.
     */
    @GetMapping("event", params = ["page"])
    fun loadEvents(
            model: Model,
            @RequestParam(value = "page") page: Int,
            @RequestParam(value = "car_id", required = false) carId: Long?
    ): String {
        val events = if (carId == null) {
            eventService.getEvents(page = page)
        } else {
            eventService.getEventsByCar(page = page, carId = carId)
        }

        model.addAttribute("events", events)
        return "fragments/event :: events"
    }

    /**
     * Method send request to data server over [eventService] to update events note in database.
     *
     * @param eventId identification number of event in database on data server which will be updated.
     * @param note is new note which will be set to event.
     */
    @PutMapping("event")
    fun updateEvent(
            @RequestParam(value = "id") eventId: Long,
            @RequestParam(value = "note") note: String
    ) {
        eventService.updateEvent(eventId, note)
    }

    /**
     * Method send request to data server over [eventService] to delete event from database.
     *
     * @param eventId is identification number of event in database on data server which will be deleted.
     */
    @DeleteMapping("event")
    fun deleteEvent(
            @RequestParam(value = "event_id") eventId: Long
    ) {
        eventService.removeEvent(eventId)
    }
}