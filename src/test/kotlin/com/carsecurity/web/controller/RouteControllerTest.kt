package com.carsecurity.web.controller


import com.carsecurity.web.rest.model.Car
import com.carsecurity.web.rest.model.Position
import com.carsecurity.web.rest.model.Route
import com.carsecurity.web.rest.service.CarService
import com.carsecurity.web.rest.service.PositionService
import com.carsecurity.web.rest.service.RouteService
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.ui.ExtendedModelMap

class RouteControllerTest {

    @Mock
    private lateinit var routeService: RouteService

    @Mock
    private lateinit var positionService: PositionService

    @Mock
    private lateinit var carService: CarService

    private lateinit var routeController: RouteController

    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse

    private val pageLimit = 3
    private val bingMapApiKey = "sdkljflsejifljsdlkjflsiejflsij94w0"
    private val routeViewName = "route"
    private val routesViewName = "routes"

    private val car1 = Car(12, "Rudolf", 0, 0, "Trabant", "")
    private val car2 = Car(13, "Randolf", 1, 2, "Varburg", "note")


    private val route1 = Route(23, 123f, 12f, 899, 32423L, 12, "Trabant", "")
    private val route2 = Route(24, 120f, 30f, 609, 32432L, 12, "Trabant", "")

    private val position1 = Position(1, 23, 1f, 2f, 3f, "2.4.1992 23:32", 4f, 5f, 6f)
    private val position2 = Position(2, 23, 7f, 8f, 9f, "2.4.1992 23:41", 10f, 11f, 12f)


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()

        routeController = RouteController(routeService, positionService, carService, pageLimit, bingMapApiKey)
    }

    @Test
    fun `get route by id success`() {

        val model = ExtendedModelMap()

        doReturn(route1).`when`(routeService).getRoute(route1.id)
        doReturn(arrayOf(position1, position2)).`when`(positionService).getPositions(route1.id)

        val result = routeController.getRouteById(route1.id, model)

        assertEquals(routeViewName, result)
        assertEquals(route1, model["route"])
        assertArrayEquals(arrayOf(position1, position2), model["positions"] as Array<*>)
        assertEquals(bingMapApiKey, model["bingKey"])
    }

    @Test
    fun `get routes null car id success`() {

        val model = ExtendedModelMap()
        val page = 324

        doReturn(arrayOf(route1, route2)).`when`(routeService).getRoutes(323)
        doReturn(2L).`when`(routeService).countRoutes()
        doReturn(arrayOf(car1, car2)).`when`(carService).getCars()

        val result = routeController.getRoutes(model, page, null)

        assertEquals(routesViewName, result)
        assertArrayEquals(arrayOf(route1, route2), model["routes"] as Array<*>)
        assertArrayEquals(arrayOf(car1, car2), model["cars"] as Array<*>)
        assertEquals(1, model["numberOfPages"])
        assertEquals(324, model["actualPage"])
        assertFalse(model.containsKey("carId"))
    }

    @Test
    fun `get routes with car id success`() {

        val model = ExtendedModelMap()

        doReturn(arrayOf(route1)).`when`(routeService).getRoutesByCar(0, 12)
        doReturn(20L).`when`(routeService).countRoutesByCar(12)
        doReturn(arrayOf(car1, car2)).`when`(carService).getCars()

        val result = routeController.getRoutes(model, 0, 12)

        assertEquals(routesViewName, result)
        assertEquals(12L, model["carId"])
        assertArrayEquals(arrayOf(route1), model["routes"] as Array<*>)
        assertArrayEquals(arrayOf(car1, car2), model["cars"] as Array<*>)
        assertEquals(7, model["numberOfPages"])
        assertEquals(1, model["actualPage"])
    }

    @Test
    fun `get map`() {

        val byteArray = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

        val longCaptor = argumentCaptor<Long>()
        doReturn(byteArray).`when`(routeService).getRouteMap(longCaptor.capture())

        val result = routeController.getMap(678765L)

        assertEquals(678765L, longCaptor.firstValue)
        assertEquals(byteArray, result)
    }

    @Test
    fun `get GPX route`() {

        val gpx = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<gpx version=\"1.1\" creator=\"JPX - https://github.com/jenetics/jpx\" xmlns=\"http://www.topografix.com/GPX/1/1\">" +
                "<trk>" +
                "<trkseg>" +
                "<trkpt lat=\"49.74919891357422\" lon=\"13.37909984588623\"><ele>355.9909973144531</ele><time>2019-04-06T14:19:30Z</time></trkpt>" +
                "<trkpt lat=\"49.7494010925293\" lon=\"13.379199981689453\"><ele>354.0329895019531</ele><time>2019-04-06T14:19:37Z</time></trkpt>" +
                "</trkseg>" +
                "</trk>" +
                "</gpx>"

        val longCaptor = argumentCaptor<Long>()
        doReturn(gpx).`when`(routeService).getRouteGPX(longCaptor.capture())

        val result = routeController.getGPXRoute(678765L)

        assertEquals(678765L, longCaptor.firstValue)
        assertEquals(gpx, result)
    }

    @Test
    fun `delete route`() {

        val longCaptor = argumentCaptor<Long>()

        routeController.deleteRoute(4566788998786L)

        verify(routeService).removeRoute(longCaptor.capture())
        assertEquals(4566788998786L, longCaptor.firstValue)
    }
}