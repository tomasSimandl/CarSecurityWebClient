package com.carsecurity.web.service


import com.carsecurity.web.rest.service.Impl.ToolServiceImpl
import com.carsecurity.web.rest.service.ToolService
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class ToolServiceTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    private lateinit var toolService: ToolService

    private val restServerUrl = "https://server.com:8080"

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        toolService = ToolServiceImpl(restServerUrl, restTemplate)
    }

    @Test
    fun `activate tool`() {

        val response = "dsklfj3829fu8flkd"
        val responseEntity = ResponseEntity(response, HttpStatus.OK)

        val urlCaptor = argumentCaptor<String>()
        val requestCaptor = argumentCaptor<Map<String, String>>()
        doReturn(responseEntity).`when`(restTemplate).postForEntity(urlCaptor.capture(), requestCaptor.capture(), eq(String::class.java))

        val result = toolService.activateTool(8329283L, "alarm")

        assertEquals("$restServerUrl/tool/activate", urlCaptor.firstValue)
        assertEquals(response, result)
        assertEquals(2, requestCaptor.firstValue.size)
        assertEquals("8329283", (requestCaptor.firstValue["car_id"] as List<*>).first())
        assertEquals("alarm", (requestCaptor.firstValue["tool"] as List<*>).first())
    }

    @Test
    fun `deactivate tool`() {

        val response = "    dsf"
        val responseEntity = ResponseEntity(response, HttpStatus.OK)

        val urlCaptor = argumentCaptor<String>()
        val requestCaptor = argumentCaptor<Map<String, String>>()
        doReturn(responseEntity).`when`(restTemplate).postForEntity(urlCaptor.capture(), requestCaptor.capture(), eq(String::class.java))

        val result = toolService.deactivateTool(1L, "tracker")

        assertEquals("$restServerUrl/tool/deactivate", urlCaptor.firstValue)
        assertEquals(response, result)
        assertEquals(2, requestCaptor.firstValue.size)
        assertEquals("1", (requestCaptor.firstValue["car_id"] as List<*>).first())
        assertEquals("tracker", (requestCaptor.firstValue["tool"] as List<*>).first())
    }
}