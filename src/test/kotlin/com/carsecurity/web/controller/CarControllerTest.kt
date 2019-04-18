package com.carsecurity.web.controller


import com.carsecurity.web.rest.model.Car
import com.carsecurity.web.rest.service.CarService
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.ui.ExtendedModelMap

class CarControllerTest {

    @Mock
    private lateinit var carService: CarService

    private lateinit var carController: CarController


    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse

    private val carViewName = "car"

    private val car1 = Car(12, "Rudolf", 0, 0, "Trabant", "")
    private val car2 = Car(13, "Randolf", 1, 2, "Varburg", "note")
    private val cars = arrayOf(car1, car2)


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()

        carController = CarController(carService)
    }

    @Test
    fun `get cars success`() {

        val model = ExtendedModelMap()

        doReturn(cars).`when`(carService).getCars()

        val result = carController.getCars(model)

        assertEquals(carViewName, result)
        assertArrayEquals(cars, model["cars"] as Array<*>)
    }

    @Test
    fun `create car success`() {

        val car1Json = Gson().toJson(car1)

        val nameCaptor = argumentCaptor<String>()
        val noteCaptor = argumentCaptor<String>()
        doReturn(car1Json).`when`(carService).createCar(nameCaptor.capture(), noteCaptor.capture())

        carController.createCar(request, response, "Trabant", "Cool note")

        assertEquals("Trabant", nameCaptor.firstValue)
        assertEquals("Cool note", noteCaptor.firstValue)
        assertEquals(car1Json, response.contentAsString)
    }

    @Test
    fun `create car error`() {

        val errorJson = "{\"error\":\"Error message\"}"

        val nameCaptor = argumentCaptor<String>()
        val noteCaptor = argumentCaptor<String>()
        doReturn(errorJson).`when`(carService).createCar(nameCaptor.capture(), noteCaptor.capture())

        carController.createCar(request, response, "kůň", "Koníček")

        assertEquals("kůň", nameCaptor.firstValue)
        assertEquals("Koníček", noteCaptor.firstValue)
        assertEquals(errorJson, response.contentAsString)
    }

    @Test
    fun `update car`() {

        val carId = 78L
        val name = "Ferrari"
        val note = "Slow car"

        carController.updateCar(carId, name, note)

        val idCaptor = argumentCaptor<Long>()
        val nameCaptor = argumentCaptor<String>()
        val noteCaptor = argumentCaptor<String>()

        verify(carService).updateCar(idCaptor.capture(), nameCaptor.capture(), noteCaptor.capture())
        assertEquals(carId, idCaptor.firstValue)
        assertEquals(name, nameCaptor.firstValue)
        assertEquals(note, noteCaptor.firstValue)
    }


    @Test
    fun `delete car`() {
        val carId = 18231L

        carController.deleteCar(carId)

        val idCaptor = argumentCaptor<Long>()

        verify(carService).removeCar(idCaptor.capture())
        assertEquals(carId, idCaptor.firstValue)
    }
}