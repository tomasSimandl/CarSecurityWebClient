package com.carsecurity.web.service


import com.carsecurity.web.rest.model.User
import com.carsecurity.web.rest.service.Impl.UserServiceImpl
import com.carsecurity.web.rest.service.UserService
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class UserServiceTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    private lateinit var userService: UserService

    private val restServerUrl = "https://server.com:8080"
    private val userMapping = "/user"

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        userService = UserServiceImpl(restServerUrl, restTemplate)
    }

    @Test
    fun `get user`() {

        val user = User(23, "Karel", "Karel@email.com")

        val urlCaptor = argumentCaptor<String>()
        val responseEntity = ResponseEntity(user, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).getForEntity(urlCaptor.capture(), eq(User::class.java))

        val result = userService.getUser(user.username)

        assertEquals("$restServerUrl$userMapping?username=${user.username}", urlCaptor.firstValue)
        assertEquals(user, result)
    }

    @Test
    fun `update users email`() {

        userService.updateUserEmail(123L, "Emil@mail.com")

        val urlCaptor = argumentCaptor<String>()
        verify(restTemplate).put(urlCaptor.capture(), eq(null))

        assertEquals("$restServerUrl$userMapping?id=123&email=Emil@mail.com", urlCaptor.firstValue)
    }

    @Test
    fun `update users password`() {

        userService.updateUserPassword(123L, "oldPassword", "newPassword")

        val urlCaptor = argumentCaptor<String>()
        verify(restTemplate).put(urlCaptor.capture(), eq(null))

        assertEquals("$restServerUrl$userMapping?id=123&old_password=oldPassword&new_password=newPassword", urlCaptor.firstValue)
    }

    @Test
    fun `delete user`() {

        userService.removeUser(83924L)

        val urlCaptor = argumentCaptor<String>()
        verify(restTemplate).delete(urlCaptor.capture())

        assertEquals("$restServerUrl$userMapping?id=83924", urlCaptor.firstValue)
    }
}