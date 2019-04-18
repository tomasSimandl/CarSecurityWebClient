package com.carsecurity.web.controller


import com.carsecurity.web.rest.model.Car
import com.carsecurity.web.rest.model.Event
import com.carsecurity.web.rest.service.CarService
import com.carsecurity.web.rest.service.EventService
import com.nhaarman.mockitokotlin2.any
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
import org.mockito.internal.verification.Times
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.ui.ExtendedModelMap

class EventControllerTest {

    @Mock
    private lateinit var carService: CarService

    @Mock
    private lateinit var eventService: EventService

    private lateinit var eventController: EventController


    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse

    private val eventViewName = "event"

    private val car1 = Car(12, "Rudolf", 0, 0, "Trabant", "")
    private val car2 = Car(13, "Randolf", 1, 2, "Varburg", "note")
    private val cars = arrayOf(car1, car2)

    private val event1 = Event(1, "Alarm", 123456789L, "Trabant", "")
    private val event2 = Event(2, "Util ON", 123456799L, "Trabant", "")
    private val event3 = Event(3, "Alarm", 123456999L, "Varburg", "")


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()

        eventController = EventController(eventService, carService)
    }

    @Test
    fun `get events null carId success`() {

        val model = ExtendedModelMap()

        doReturn(arrayOf(event1, event2, event3)).`when`(eventService).getEvents(0)
        doReturn(cars).`when`(carService).getCars()

        val result = eventController.getEvents(model, null)

        assertEquals(eventViewName, result)
        assertArrayEquals(arrayOf(event1, event2, event3), model["events"] as Array<*>)
        assertArrayEquals(cars, model["cars"] as Array<*>)
        assertEquals("/event?page=", model["loadNextUrl"])
        verify(eventService, Times(0)).getEventsByCar(any(), any())
    }

    @Test
    fun `get events with carId success`() {

        val model = ExtendedModelMap()

        val idCaptor = argumentCaptor<Long>()
        doReturn(arrayOf(event1, event2)).`when`(eventService).getEventsByCar(eq(0), idCaptor.capture())
        doReturn(cars).`when`(carService).getCars()

        val result = eventController.getEvents(model, 12)

        assertEquals(12, idCaptor.firstValue)
        assertEquals(eventViewName, result)
        assertArrayEquals(arrayOf(event1, event2), model["events"] as Array<*>)
        assertArrayEquals(cars, model["cars"] as Array<*>)
        assertEquals("/event?car_id=12&page=", model["loadNextUrl"])
        verify(eventService, Times(0)).getEvents(any())
    }

    @Test
    fun `load events null carId success`() {

        val model = ExtendedModelMap()
        val pageCaptor = argumentCaptor<Int>()

        doReturn(arrayOf(event1)).`when`(eventService).getEvents(pageCaptor.capture())

        val result = eventController.loadEvents(model, 342, null)

        assertEquals(342, pageCaptor.firstValue)
        assertEquals("fragments/event :: events", result)
        assertArrayEquals(arrayOf(event1), model["events"] as Array<*>)
        verify(eventService, Times(0)).getEventsByCar(any(), any())
    }

    @Test
    fun `load events with carId success`() {

        val model = ExtendedModelMap()
        val pageCaptor = argumentCaptor<Int>()
        val idCaptor = argumentCaptor<Long>()

        doReturn(arrayOf(event2)).`when`(eventService).getEventsByCar(pageCaptor.capture(), idCaptor.capture())

        val result = eventController.loadEvents(model, 342, 87)

        assertEquals(342, pageCaptor.firstValue)
        assertEquals(87, idCaptor.firstValue)
        assertEquals("fragments/event :: events", result)
        assertArrayEquals(arrayOf(event2), model["events"] as Array<*>)
        verify(eventService, Times(0)).getEvents(any())
    }

    @Test
    fun `update event`() {

        val note = "Notes super updated note"
        val id = 687987687L

        eventController.updateEvent(id, note)

        val idCaptor = argumentCaptor<Long>()
        val noteCaptor = argumentCaptor<String>()
        verify(eventService).updateEvent(idCaptor.capture(), noteCaptor.capture())

        assertEquals(id, idCaptor.firstValue)
        assertEquals(note, noteCaptor.firstValue)
    }

    @Test
    fun `delete event`() {

        val id = 43905345L

        eventController.deleteEvent(id)

        val idCaptor = argumentCaptor<Long>()
        verify(eventService).removeEvent(idCaptor.capture())
        assertEquals(id, idCaptor.firstValue)
    }
}