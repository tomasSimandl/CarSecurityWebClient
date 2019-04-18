package com.carsecurity.web.service


import com.carsecurity.web.rest.model.Event
import com.carsecurity.web.rest.service.EventService
import com.carsecurity.web.rest.service.Impl.EventServiceImpl
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class EventServiceTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    private lateinit var eventService: EventService

    private val restServerUrl = "https://server.com:8080"
    private val eventMapping = "/event"
    private val pageLimit = 3

    private val event1 = Event(1, "Alarm", 123456789L, "Trabant", "")
    private val event2 = Event(2, "Util ON", 123456799L, "Trabant", "")
    private val event3 = Event(3, "Alarm", 123456999L, "Varburg", "")


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        eventService = EventServiceImpl(restServerUrl, pageLimit, restTemplate)
    }

    @Test
    fun `get events`() {

        val events = arrayOf(event1, event2, event3)

        val urlCaptor = argumentCaptor<String>()
        val responseEntity = ResponseEntity(events, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).getForEntity(urlCaptor.capture(), eq(Array<Event>::class.java))


        val result = eventService.getEvents(32)
        assertEquals("$restServerUrl$eventMapping?page=32&limit=$pageLimit", urlCaptor.firstValue)
        assertArrayEquals(events, result)
    }

    @Test
    fun `get events by car`() {

        val events = arrayOf(event2, event1)

        val urlCaptor = argumentCaptor<String>()
        val responseEntity = ResponseEntity(events, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).getForEntity(urlCaptor.capture(), eq(Array<Event>::class.java))


        val result = eventService.getEventsByCar(21, 8494L)
        assertEquals("$restServerUrl$eventMapping?page=21&limit=$pageLimit&car_id=8494", urlCaptor.firstValue)
        assertArrayEquals(events, result)
    }

    @Test
    fun `update event`() {

        eventService.updateEvent(7654L, "new updated note")

        val urlCaptor = argumentCaptor<String>()
        val httpEntityCaptor = argumentCaptor<HttpEntity<*>>()
        verify(restTemplate).put(urlCaptor.capture(), httpEntityCaptor.capture())


        assertEquals("$restServerUrl$eventMapping", urlCaptor.firstValue)
        assertEquals("7654", (httpEntityCaptor.firstValue.body as Map<*, *>)["id"])
        assertEquals("new updated note", (httpEntityCaptor.firstValue.body as Map<*, *>)["note"])
    }

    @Test
    fun `remove event`() {

        eventService.removeEvent(842L)

        val urlCaptor = argumentCaptor<String>()
        verify(restTemplate).delete(urlCaptor.capture())

        assertEquals("$restServerUrl$eventMapping?event_id=842", urlCaptor.firstValue)
    }
}