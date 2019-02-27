package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Event


interface EventService {

    fun getEvents(page: Int): Array<Event>
    fun getEventsByCar(page: Int, carId: Long): Array<Event>
    fun updateEvent(id: Long, note: String)
    fun removeEvent(eventId: Long)
}