package com.carsecurity.web.controller


import com.carsecurity.web.rest.service.ToolService
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.springframework.ui.ExtendedModelMap

class ToolControllerTest {

    @Mock
    private lateinit var toolService: ToolService

    private lateinit var toolController: ToolController

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        toolController = ToolController(toolService)
    }

    @Test
    fun `activate tool`() {

        val model = ExtendedModelMap()
        val longCaptor = argumentCaptor<Long>()
        val stringCaptor = argumentCaptor<String>()

        toolController.activateTool(model, 324832L, "alarm")

        verify(toolService).activateTool(longCaptor.capture(), stringCaptor.capture())

        assertEquals(324832L, longCaptor.firstValue)
        assertEquals("alarm", stringCaptor.firstValue)
    }

    @Test
    fun `deactivate tool`() {

        val model = ExtendedModelMap()
        val longCaptor = argumentCaptor<Long>()
        val stringCaptor = argumentCaptor<String>()

        toolController.deactivateTool(model, 1L, "tracker")

        verify(toolService).deactivateTool(longCaptor.capture(), stringCaptor.capture())

        assertEquals(1, longCaptor.firstValue)
        assertEquals("tracker", stringCaptor.firstValue)
    }
}