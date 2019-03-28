package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Event
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * Implementation of service which is used for sending requests to data server.
 *
 * @param restServerUrl is url address of data server
 * @param restTemplate is template which can be used for sending authorized requests to data server.
 * @param pageLimit is maximal limit of events on one page.
 */
@Service
class EventServiceImpl(

        @Value("\${rest.server.url}")
        private val restServerUrl: String,

        @Value("\${web.data.load.page.limit}")
        private val pageLimit: Int,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : EventService {

    /**
     * Method send request to data server to get users events from [page] to [pageLimit].
     * Result is returned as an array of events.
     *
     * @param page number of requested page.
     * @return array of founded events.
     */
    override fun getEvents(page: Int): Array<Event> {

        val url = "$restServerUrl$EVENT_MAPPING?page=$page&limit=$pageLimit"

        val eventEntity = restTemplate.getForEntity(url, Array<Event>::class.java)
        return eventEntity.body!!
    }

    /**
     * Method send request to data server to get events from [page] to [pageLimit] which was created by [carId].
     *
     * @param page number of requested page.
     * @param carId identification number of event in database on data server.
     * @return array of found events.
     */
    override fun getEventsByCar(page: Int, carId: Long): Array<Event> {
        val url = "$restServerUrl$EVENT_MAPPING?page=$page&limit=$pageLimit&car_id=$carId"

        val eventEntity = restTemplate.getForEntity(url, Array<Event>::class.java)
        return eventEntity.body!!
    }

    /**
     * Method send request to data server to update events note.
     *
     * @param id is identification number of event in database on data server.
     * @param note is new note about event.
     */
    override fun updateEvent(id: Long, note: String) {
        val url = "$restServerUrl$EVENT_MAPPING"

        val form = HashMap<String, String>()
        form["id"] = id.toString()
        form["note"] = note

        val httpEntity = HttpEntity(form)
        restTemplate.put(url, httpEntity)
    }

    /**
     * Method send request do data server to remove event specified by [eventId]
     *
     * @param eventId is identification of event which will be deleted from database on data server.
     */
    override fun removeEvent(eventId: Long) {
        val url = "$restServerUrl$EVENT_MAPPING?event_id=$eventId"
        restTemplate.delete(url)
    }
}