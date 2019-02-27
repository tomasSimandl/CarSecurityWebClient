package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Event
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class EventServiceImpl(

        @Value("\${rest.server.url}")
        private val restServerUrl: String,

        @Value("\${web.data.load.page.limit}")
        private val pageLimit: Int,

        @Qualifier("tokenRestTemplate")
        private val restTemplate: RestTemplate
) : EventService {

    override fun getEvents(page: Int): Array<Event> {

        val url = "$restServerUrl$EVENT_MAPPING?page=$page&limit=$pageLimit"

        val eventEntity = restTemplate.getForEntity(url, Array<Event>::class.java)
        return eventEntity.body!!
    }

    override fun getEventsByCar(page: Int, carId: Long): Array<Event> {
        val url = "$restServerUrl$EVENT_MAPPING?page=$page&limit=$pageLimit&car_id=$carId"

        val eventEntity = restTemplate.getForEntity(url, Array<Event>::class.java)
        return eventEntity.body!!
    }

    override fun updateEvent(id: Long, note: String) {
        val url = "$restServerUrl$EVENT_MAPPING"

        val form = HashMap<String, String>()
        form["id"] = id.toString()
        form["note"] = note

        val httpEntity = HttpEntity(form)
        restTemplate.put(url, httpEntity)
    }

    override fun removeEvent(eventId: Long) {
        val url = "$restServerUrl$EVENT_MAPPING?event_id=$eventId"
        restTemplate.delete(url)
    }
}