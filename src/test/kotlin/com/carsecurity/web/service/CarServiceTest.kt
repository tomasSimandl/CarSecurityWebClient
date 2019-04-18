package com.carsecurity.web.service


import com.carsecurity.web.rest.model.Car
import com.carsecurity.web.rest.service.CarService
import com.carsecurity.web.rest.service.Impl.CarServiceImpl
import com.google.gson.Gson
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

class CarServiceTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    private lateinit var carService: CarService

    private val restServerUrl = "https://server.com:8080"
    private val carMapping = "/car"

    private val car1 = Car(12, "Rudolf", 0, 0, "Trabant", "")
    private val car2 = Car(13, "Randolf", 1, 2, "Varburg", "note")
    private val cars = arrayOf(car1, car2)


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        carService = CarServiceImpl(restServerUrl, restTemplate)
    }

    @Test
    fun `get cars`() {

        val urlCaptor = argumentCaptor<String>()
        val responseEntity = ResponseEntity(cars, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).getForEntity(urlCaptor.capture(), eq(Array<Car>::class.java))

        val result = carService.getCars()

        assertEquals("$restServerUrl$carMapping", urlCaptor.firstValue)
        assertArrayEquals(cars, result)
    }

    @Test
    fun `create car`() {

        val name = "Peugeot"
        val note = "car"

        val urlCaptor = argumentCaptor<String>()
        val httpEntityCaptor = argumentCaptor<HttpEntity<*>>()
        val responseEntity = ResponseEntity(Gson().toJson(car1), HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).postForEntity(urlCaptor.capture(), httpEntityCaptor.capture(), eq(String::class.java))

        val result = carService.createCar(name, note)

        assertEquals("$restServerUrl$carMapping", urlCaptor.firstValue)
        assertEquals(Gson().toJson(car1), result)
        assertEquals(name, (httpEntityCaptor.firstValue.body as Map<*, *>)["name"])
        assertEquals(note, (httpEntityCaptor.firstValue.body as Map<*, *>)["note"])
    }

    @Test
    fun `update car`() {

        val id = 678L
        val name = "Peugeot"
        val note = "car"

        carService.updateCar(id, name, note)

        val urlCaptor = argumentCaptor<String>()
        val httpEntityCaptor = argumentCaptor<HttpEntity<*>>()
        verify(restTemplate).put(urlCaptor.capture(), httpEntityCaptor.capture())

        assertEquals("$restServerUrl$carMapping", urlCaptor.firstValue)
        assertEquals("$id", (httpEntityCaptor.firstValue.body as Map<*, *>)["id"])
        assertEquals(name, (httpEntityCaptor.firstValue.body as Map<*, *>)["name"])
        assertEquals(note, (httpEntityCaptor.firstValue.body as Map<*, *>)["note"])
    }

    @Test
    fun `remove car`() {

        val id = 9376L

        carService.removeCar(id)

        val urlCaptor = argumentCaptor<String>()
        verify(restTemplate).delete(urlCaptor.capture())

        assertEquals("$restServerUrl$carMapping?car_id=$id", urlCaptor.firstValue)
    }

    @Test
    fun `remove cars`() {

        carService.removeCars()

        val urlCaptor = argumentCaptor<String>()
        verify(restTemplate).delete(urlCaptor.capture())

        assertEquals("$restServerUrl$carMapping", urlCaptor.firstValue)
    }
}