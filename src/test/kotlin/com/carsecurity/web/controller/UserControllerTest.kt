package com.carsecurity.web.controller


import com.carsecurity.web.rest.model.Car
import com.carsecurity.web.rest.model.Position
import com.carsecurity.web.rest.model.Route
import com.carsecurity.web.rest.service.CarService
import com.carsecurity.web.rest.service.LoginService
import com.carsecurity.web.rest.service.UserService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.ui.ExtendedModelMap
import org.springframework.web.client.HttpClientErrorException

class UserControllerTest {

    @Mock
    private lateinit var loginService: LoginService

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var carService: CarService

    private lateinit var userController: UserController

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        userController = UserController(loginService, userService, carService)
    }

    @Test
    fun `login page success`() {

        val result = userController.login(null)
        assertEquals("login", result)
    }

    @Test
    fun `login page already login`() {

        val principal = TestingAuthenticationToken("principal", "credentials")

        val result = userController.login(principal)
        assertEquals("forward:/", result)
    }

    @Test
    fun `register page success`() {

        val result = userController.register(null)
        assertEquals("register", result)
    }

    @Test
    fun `register page already login`() {

        val principal = TestingAuthenticationToken("principal", "credentials")

        val result = userController.register(principal)
        assertEquals("forward:/", result)
    }

    @Test
    fun `register post success`() {

        val model = ExtendedModelMap()
        val username = "emil"
        val pass = "12345678"
        val passConfirm = "12345678"

        doReturn(201).`when`(loginService).register(username, pass)

        val result = userController.registerPost(model, username, pass, passConfirm)

        assertFalse(model.containsKey("error"))
        assertEquals("redirect:/login?registered", result)
    }

    @Test
    fun `register post blank username`() {

        val model = ExtendedModelMap()
        val username = " "
        val pass = "12345678"
        val passConfirm = "12345678"

        val result = userController.registerPost(model, username, pass, passConfirm)

        verify(loginService, Times(0)).login(any(), any())

        assertTrue(model.containsKey("error"))
        assertEquals("register", result)
    }

    @Test
    fun `register post blank password`() {

        val model = ExtendedModelMap()
        val username = "emil"
        val pass = "    "
        val passConfirm = "12345678"

        val result = userController.registerPost(model, username, pass, passConfirm)

        verify(loginService, Times(0)).login(any(), any())

        assertTrue(model.containsKey("error"))
        assertEquals("register", result)
    }

    @Test
    fun `register post blank password confirmation`() {

        val model = ExtendedModelMap()
        val username = "emil"
        val pass = "12345678"
        val passConfirm = ""

        val result = userController.registerPost(model, username, pass, passConfirm)

        verify(loginService, Times(0)).login(any(), any())

        assertTrue(model.containsKey("error"))
        assertEquals("register", result)
    }

    @Test
    fun `register post not match password`() {

        val model = ExtendedModelMap()
        val username = "emil"
        val pass = "87654321"
        val passConfirm = "12345678"

        val result = userController.registerPost(model, username, pass, passConfirm)

        verify(loginService, Times(0)).login(any(), any())

        assertTrue(model.containsKey("error"))
        assertEquals("register", result)
    }

    @Test
    fun `register post short password`() {

        val model = ExtendedModelMap()
        val username = "emil"
        val pass = "1234"
        val passConfirm = "1234"

        val result = userController.registerPost(model, username, pass, passConfirm)

        verify(loginService, Times(0)).login(any(), any())

        assertTrue(model.containsKey("error"))
        assertEquals("register", result)
    }

    @Test
    fun `register post server response 400`() {

        val model = ExtendedModelMap()
        val username = "emil"
        val pass = "12345678"
        val passConfirm = "12345678"

        doReturn(400).`when`(loginService).register(username, pass)

        val result = userController.registerPost(model, username, pass, passConfirm)

        verify(loginService).register(any(), any())

        assertTrue(model.containsKey("error"))
        assertEquals("register", result)
    }

    @Test
    fun `register post server response 500`() {

        val model = ExtendedModelMap()
        val username = "emil"
        val pass = "12345678"
        val passConfirm = "12345678"

        doReturn(500).`when`(loginService).register(username, pass)

        val result = userController.registerPost(model, username, pass, passConfirm)

        verify(loginService).register(any(), any())

        assertTrue(model.containsKey("error"))
        assertEquals("register", result)
    }


    @Test
    fun `update email success`() {

        val id = 83924L
        val email = "mail@m.com"

        userController.updateEmail(id, email)

        val longCaptor = argumentCaptor<Long>()
        val stringCaptor = argumentCaptor<String>()
        verify(userService).updateUserEmail(longCaptor.capture(), stringCaptor.capture())

        assertEquals(id, longCaptor.firstValue)
        assertEquals(email, stringCaptor.firstValue)
    }

    @Test
    fun `update password success`() {

        val userId = 12L
        val oldPass = "12345678"
        val newPass = "87654321"
        val newPassConf = "87654321"

        userController.updatePassword(userId, oldPass, newPass, newPassConf)


        val idCaptor = argumentCaptor<Long>()
        val oldPassCaptor = argumentCaptor<String>()
        val newPassCaptor = argumentCaptor<String>()
        verify(userService).updateUserPassword(idCaptor.capture(), oldPassCaptor.capture(), newPassCaptor.capture())

        assertEquals(userId, idCaptor.firstValue)
        assertEquals(oldPass, oldPassCaptor.firstValue)
        assertEquals(newPass, newPassCaptor.firstValue)
    }

    @Test
    fun `update password blank old password`() {

        val userId = 12L
        val oldPass = " "
        val newPass = "87654321"
        val newPassConf = "87654321"

        try {
            userController.updatePassword(userId, oldPass, newPass, newPassConf)
            fail()
        } catch (e: HttpClientErrorException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
        }
    }

    @Test
    fun `update password blank new password`() {

        val userId = 12L
        val oldPass = "12345678"
        val newPass = " "
        val newPassConf = "87654321"

        try {
            userController.updatePassword(userId, oldPass, newPass, newPassConf)
            fail()
        } catch (e: HttpClientErrorException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
        }
    }

    @Test
    fun `update password blank new password confirmation`() {

        val userId = 12L
        val oldPass = "12345678"
        val newPass = "87654321"
        val newPassConf = ""

        try {
            userController.updatePassword(userId, oldPass, newPass, newPassConf)
            fail()
        } catch (e: HttpClientErrorException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
        }
    }

    @Test
    fun `update password not match passwords`() {

        val userId = 12L
        val oldPass = "12345678"
        val newPass = "abcdefgh"
        val newPassConf = "87654321"

        try {
            userController.updatePassword(userId, oldPass, newPass, newPassConf)
            fail()
        } catch (e: HttpClientErrorException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
        }
    }

    @Test
    fun `update password short password`() {

        val userId = 12L
        val oldPass = "12345678"
        val newPass = "abcd"
        val newPassConf = "abcd"

        try {
            userController.updatePassword(userId, oldPass, newPass, newPassConf)
            fail()
        } catch (e: HttpClientErrorException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
        }
    }

    @Test
    fun `update password server return 401`() {

        val userId = 12L
        val oldPass = "12345678"
        val newPass = "87654321"
        val newPassConf = "87654321"

        doThrow(HttpClientErrorException(HttpStatus.UNAUTHORIZED)).`when`(userService).updateUserPassword(any(), any(), any())

        try {
            userController.updatePassword(userId, oldPass, newPass, newPassConf)
            fail()
        } catch (e: HttpClientErrorException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
        }
    }

    @Test
    fun `update password server return 404`() {

        val userId = 12L
        val oldPass = "12345678"
        val newPass = "87654321"
        val newPassConf = "87654321"

        doThrow(HttpClientErrorException(HttpStatus.NOT_FOUND)).`when`(userService).updateUserPassword(any(), any(), any())

        try {
            userController.updatePassword(userId, oldPass, newPass, newPassConf)
            fail()
        } catch (e: HttpClientErrorException) {
            assertEquals(HttpStatus.NOT_FOUND, e.statusCode)
        }
    }

    @Test
    fun `delete user`() {

        userController.deleteUser(234L)

        val longCaptor = argumentCaptor<Long>()
        verify(carService).removeCars()
        verify(userService).removeUser(longCaptor.capture())
        assertEquals(234L, longCaptor.firstValue)
    }
}