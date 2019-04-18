package com.carsecurity.web.controller


import com.carsecurity.web.rest.model.Car
import com.carsecurity.web.rest.model.Status
import com.carsecurity.web.rest.service.CarService
import com.carsecurity.web.rest.service.StatusService
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.MockitoAnnotations
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.ui.ExtendedModelMap

class HomeControllerTest {

    @Mock
    private lateinit var carService: CarService

    @Mock
    private lateinit var statusService: StatusService

    private lateinit var homeController: HomeController


    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse

    private val statusViewName = "status"

    private val car1 = Car(12, "Rudolf", 0, 0, "Trabant", "")
    private val car2 = Car(13, "Randolf", 1, 2, "Varburg", "note")
    private val cars = arrayOf(car1, car2)

    private val status1 = Status(0.12f, true, true, mapOf(Pair("Alarm", true), Pair("Tracker", false)), 234567L, 12)


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()

        homeController = HomeController(carService, statusService)
    }

    @Test
    fun `status page success`() {

        val model = ExtendedModelMap()

        doReturn(cars).`when`(carService).getCars()

        val result = homeController.statusPage(model)

        assertEquals(statusViewName, result)
        assertArrayEquals(cars, model["cars"] as Array<*>)
    }


    @Test
    fun `status page empty`() {

        val model = ExtendedModelMap()

        doReturn(emptyArray<Car>()).`when`(carService).getCars()

        val result = homeController.statusPage(model)

        assertEquals(statusViewName, result)
        assertTrue((model["cars"] as Array<*>).isEmpty())
    }

    @Test
    fun `get status success`() {

        val model = ExtendedModelMap()
        val carId = 12L

        doReturn(status1).`when`(statusService).getStatus(carId)

        val result = homeController.getStatus(model, carId)

        assertEquals("fragments/status :: statusFragment", result)
        assertEquals(status1, model["status"])
    }
}