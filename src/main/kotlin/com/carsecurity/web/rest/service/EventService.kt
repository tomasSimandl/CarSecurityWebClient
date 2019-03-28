package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Event

/**
 * Service which is used for sending requests to data server.
 */
interface EventService {

    /**
     * Method send request to data server to get users events from [page].
     * Result is returned as an array of events.
     *
     * @param page number of requested page.
     * @return array of founded events.
     */
    fun getEvents(page: Int): Array<Event>

    /**
     * Method send request to data server to get events from [page] which was created by [carId].
     *
     * @param page number of requested page.
     * @param carId identification number of event in database on data server.
     * @return array of found events.
     */
    fun getEventsByCar(page: Int, carId: Long): Array<Event>

    /**
     * Method send request to data server to update events note.
     *
     * @param id is identification number of event in database on data server.
     * @param note is new note about event.
     */
    fun updateEvent(id: Long, note: String)

    /**
     * Method send request do data server to remove event specified by [eventId]
     *
     * @param eventId is identification of event which will be deleted from database on data server.
     */
    fun removeEvent(eventId: Long)
}