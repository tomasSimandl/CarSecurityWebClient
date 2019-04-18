package com.carsecurity.web.controller


import com.carsecurity.web.rest.model.User
import com.carsecurity.web.rest.service.UserService
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.MockitoAnnotations
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.ui.ExtendedModelMap

class SettingsControllerTest {

    @Mock
    private lateinit var userService: UserService

    private lateinit var settingsController: SettingsController

    private val settingsViewName = "settings"

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        settingsController = SettingsController(userService)
    }

    @Test
    fun `user setting page`() {

        val model = ExtendedModelMap()
        val user = User(21, "Paul", "paul@mail.com")
        val principal = TestingAuthenticationToken("Paul", null)

        val stringCaptor = argumentCaptor<String>()
        doReturn(user).`when`(userService).getUser(stringCaptor.capture())

        val result = settingsController.userSettingPage(principal, model)

        assertEquals(settingsViewName, result)
        assertEquals(user, model["user"])
        assertEquals("Paul", stringCaptor.firstValue)
    }
}