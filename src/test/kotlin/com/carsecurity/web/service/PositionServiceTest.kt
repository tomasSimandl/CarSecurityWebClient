package com.carsecurity.web.service


import com.carsecurity.web.rest.model.Position
import com.carsecurity.web.rest.service.Impl.PositionServiceImpl
import com.carsecurity.web.rest.service.PositionService
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class PositionServiceTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    private lateinit var positionService: PositionService

    private val restServerUrl = "https://server.com:8080"
    private val positionMapping = "/position"

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        positionService = PositionServiceImpl(restServerUrl, restTemplate)
    }

    @Test
    fun `get positions`() {

        val position1 = Position(1, 23, 1f, 2f, 3f, "2.4.1992 23:32", 4f, 5f, 6f)
        val position2 = Position(2, 23, 7f, 8f, 9f, "2.4.1992 23:41", 10f, 11f, 12f)
        val positions = arrayOf(position1, position2)

        val urlCaptor = argumentCaptor<String>()
        val responseEntity = ResponseEntity(positions, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).getForEntity(urlCaptor.capture(), eq(Array<Position>::class.java))

        val result = positionService.getPositions(3892L)
        assertEquals("$restServerUrl$positionMapping?route_id=3892&limit=${Int.MAX_VALUE}", urlCaptor.firstValue)
        assertArrayEquals(positions, result)
    }
}