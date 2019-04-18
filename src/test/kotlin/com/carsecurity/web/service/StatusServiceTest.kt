package com.carsecurity.web.service


import com.carsecurity.web.rest.model.Status
import com.carsecurity.web.rest.service.Impl.StatusServiceImpl
import com.carsecurity.web.rest.service.StatusService
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

class StatusServiceTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    private lateinit var statusService: StatusService

    private val restServerUrl = "https://server.com:8080"
    private val statusMapping = "/status"

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        statusService = StatusServiceImpl(restServerUrl, restTemplate)
    }

    @Test
    fun `get status`() {

        val status = Status(0.85f, false, false, mapOf(Pair("Alarm", false)), 456789L, 23421L)

        val urlCaptor = argumentCaptor<String>()
        val responseEntity = ResponseEntity(status, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).getForEntity(urlCaptor.capture(), eq(Status::class.java))

        val result = statusService.getStatus(status.carId)

        assertEquals("$restServerUrl$statusMapping?car_id=${status.carId}", urlCaptor.firstValue)
        assertEquals(status, result)
    }
}