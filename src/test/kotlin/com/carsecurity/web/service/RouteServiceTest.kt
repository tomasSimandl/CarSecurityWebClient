package com.carsecurity.web.service


import com.carsecurity.web.rest.model.Count
import com.carsecurity.web.rest.model.Route
import com.carsecurity.web.rest.service.Impl.RouteServiceImpl
import com.carsecurity.web.rest.service.RouteService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.springframework.http.*
import org.springframework.web.client.RestTemplate

class RouteServiceTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    private lateinit var routeService: RouteService

    private val pageLimit = 7
    private val restServerUrl = "https://server.com:8080"
    private val routeMapping = "/route"

    private val route1 = Route(23, 123f, 12f, 899, 32423L, 12, "Trabant", "")
    private val route2 = Route(24, 120f, 30f, 609, 32432L, 12, "Trabant", "")
    private val routes = arrayOf(route1, route2)

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        routeService = RouteServiceImpl(restServerUrl, pageLimit, restTemplate)
    }

    @Test
    fun `get route`() {

        val urlCaptor = argumentCaptor<String>()
        val responseEntity = ResponseEntity(route1, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).getForEntity(urlCaptor.capture(), eq(Route::class.java))

        val result = routeService.getRoute(route1.id)
        assertEquals("$restServerUrl$routeMapping?route_id=${route1.id}", urlCaptor.firstValue)
        assertEquals(route1, result)
    }

    @Test
    fun `get routes`() {

        val urlCaptor = argumentCaptor<String>()
        val responseEntity = ResponseEntity(routes, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).getForEntity(urlCaptor.capture(), eq(Array<Route>::class.java))

        val result = routeService.getRoutes(9274)
        assertEquals("$restServerUrl$routeMapping?page=9274&limit=$pageLimit", urlCaptor.firstValue)
        assertArrayEquals(routes, result)
    }

    @Test
    fun `get routes by car`() {

        val urlCaptor = argumentCaptor<String>()
        val responseEntity = ResponseEntity(routes, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).getForEntity(urlCaptor.capture(), eq(Array<Route>::class.java))

        val result = routeService.getRoutesByCar(9274, 823L)
        assertEquals("$restServerUrl$routeMapping?page=9274&limit=$pageLimit&car_id=823", urlCaptor.firstValue)
        assertArrayEquals(routes, result)
    }

    @Test
    fun `get route map`() {

        val byteArray = byteArrayOf(1, 2, 3, 4, 7, 5, 3, 7, 8)
        val urlCaptor = argumentCaptor<String>()
        val httpEntityCaptor = argumentCaptor<HttpEntity<*>>()
        val responseEntity = ResponseEntity(byteArray, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).exchange(urlCaptor.capture(), eq(HttpMethod.GET), httpEntityCaptor.capture(), eq(ByteArray::class.java))

        val result = routeService.getRouteMap(3829L)
        assertEquals("$restServerUrl$routeMapping/map?route_id=3829", urlCaptor.firstValue)
        assertArrayEquals(byteArray, result)
        assertEquals(1, httpEntityCaptor.firstValue.headers.accept.size)
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, httpEntityCaptor.firstValue.headers.accept.first())
    }

    @Test
    fun `get route map empty result`() {
        val responseEntity = ResponseEntity<ByteArray?>(null, HttpStatus.BAD_REQUEST)
        doReturn(responseEntity).`when`(restTemplate).exchange(any<String>(), eq(HttpMethod.GET), any(), eq(ByteArray::class.java))

        val result = routeService.getRouteMap(3829L)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `get route GPX success`() {
        val gpx = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<gpx version=\"1.1\" creator=\"JPX - https://github.com/jenetics/jpx\" xmlns=\"http://www.topografix.com/GPX/1/1\">" +
                "<trk>" +
                "<trkseg>" +
                "<trkpt lat=\"49.74919891357422\" lon=\"13.37909984588623\"><ele>355.9909973144531</ele><time>2019-04-06T14:19:30Z</time></trkpt>" +
                "<trkpt lat=\"49.7494010925293\" lon=\"13.379199981689453\"><ele>354.0329895019531</ele><time>2019-04-06T14:19:37Z</time></trkpt>" +
                "</trkseg>" +
                "</trk>" +
                "</gpx>"

        val urlCaptor = argumentCaptor<String>()
        val responseEntity = ResponseEntity(gpx, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).getForEntity(urlCaptor.capture(), eq(String::class.java))

        val result = routeService.getRouteGPX(6789L)

        assertEquals("$restServerUrl$routeMapping/export?route_id=6789", urlCaptor.firstValue)
        assertEquals(gpx, result)
    }

    @Test
    fun `get route GPX empty result`() {

        val urlCaptor = argumentCaptor<String>()
        val responseEntity = ResponseEntity<String>(null, HttpStatus.FORBIDDEN)
        doReturn(responseEntity).`when`(restTemplate).getForEntity(urlCaptor.capture(), eq(String::class.java))

        val result = routeService.getRouteGPX(1L)

        assertEquals("$restServerUrl$routeMapping/export?route_id=1", urlCaptor.firstValue)
        assertEquals("", result)
    }

    @Test
    fun `count routes success`() {
        val count = Count(89)

        val urlCaptor = argumentCaptor<String>()
        val responseEntity = ResponseEntity(count, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).getForEntity(urlCaptor.capture(), eq(Count::class.java))

        val result = routeService.countRoutes()

        assertEquals("$restServerUrl$routeMapping/count", urlCaptor.firstValue)
        assertEquals(89, result)
    }

    @Test
    fun `count routes by car`() {
        val count = Count(0)

        val urlCaptor = argumentCaptor<String>()
        val responseEntity = ResponseEntity(count, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).getForEntity(urlCaptor.capture(), eq(Count::class.java))

        val result = routeService.countRoutesByCar(10293847L)

        assertEquals("$restServerUrl$routeMapping/count?car_id=10293847", urlCaptor.firstValue)
        assertEquals(0, result)
    }

    @Test
    fun `update route`() {

        routeService.updateRoute(382L, "some new note")

        val urlCaptor = argumentCaptor<String>()
        val httpEntityCaptor = argumentCaptor<HttpEntity<*>>()
        verify(restTemplate).put(urlCaptor.capture(), httpEntityCaptor.capture())

        assertEquals("$restServerUrl$routeMapping", urlCaptor.firstValue)
        assertEquals("382", (httpEntityCaptor.firstValue.body as Map<*, *>)["id"])
        assertEquals("some new note", (httpEntityCaptor.firstValue.body as Map<*, *>)["note"])
    }

    @Test
    fun `remove route`() {

        routeService.removeRoute(2883L)

        val urlCaptor = argumentCaptor<String>()
        verify(restTemplate).delete(urlCaptor.capture())

        assertEquals("$restServerUrl$routeMapping?route_id=2883", urlCaptor.firstValue)
    }
}