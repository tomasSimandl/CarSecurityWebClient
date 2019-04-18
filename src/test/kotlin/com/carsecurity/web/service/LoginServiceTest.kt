package com.carsecurity.web.service


import com.carsecurity.web.rest.model.Token
import com.carsecurity.web.rest.service.Impl.LoginServiceImpl
import com.carsecurity.web.rest.service.LoginService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.util.*

class LoginServiceTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    private lateinit var loginService: LoginService

    private val authServerUrl = "https://server.com:8080"
    private val clientId = "client"
    private val clientSecret = "secret"

    private val tokenMapping = "/oauth/token"
    private val userMapping = "/user"


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        loginService = LoginServiceImpl(authServerUrl, clientId, clientSecret, restTemplate)
    }

    @Test
    fun login() {

        val token = Token("access token", "barer", "refresh token", 534L, "scope", Date().time)
        val name = "emanuel"
        val pass = "password"

        val urlCaptor = argumentCaptor<String>()
        val httpEntityCaptor = argumentCaptor<HttpEntity<*>>()
        val responseEntity = ResponseEntity(token, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).postForEntity(urlCaptor.capture(), httpEntityCaptor.capture(), eq(Token::class.java))


        val result = loginService.login(name, pass)
        assertEquals("$authServerUrl$tokenMapping", urlCaptor.firstValue)
        assertEquals(token, result)
        assertEquals(name, ((httpEntityCaptor.firstValue.body as Map<*, *>)["username"] as List<*>).first())
        assertEquals(pass, ((httpEntityCaptor.firstValue.body as Map<*, *>)["password"] as List<*>).first())
        assertEquals("password", ((httpEntityCaptor.firstValue.body as Map<*, *>)["grant_type"] as List<*>).first())
        assertEquals(MediaType.APPLICATION_FORM_URLENCODED, httpEntityCaptor.firstValue.headers.contentType)

        val encodedPass = Base64.getEncoder().encode("$clientId:$clientSecret".toByteArray())
        assertEquals("Basic ${String(encodedPass)}", (httpEntityCaptor.firstValue.headers["Authorization"] as List<*>).first())
    }

    @Test
    fun `refresh token`() {

        val token = Token("access token", "barer", "refresh token", 534L, "scope", Date().time)
        val refreshToken = "old refresh token"

        val urlCaptor = argumentCaptor<String>()
        val httpEntityCaptor = argumentCaptor<HttpEntity<*>>()
        val responseEntity = ResponseEntity(token, HttpStatus.OK)
        doReturn(responseEntity).`when`(restTemplate).postForEntity(urlCaptor.capture(), httpEntityCaptor.capture(), eq(Token::class.java))


        val result = loginService.refresh(refreshToken)

        assertEquals("$authServerUrl$tokenMapping", urlCaptor.firstValue)
        assertEquals(token, result)
        assertEquals(refreshToken, ((httpEntityCaptor.firstValue.body as Map<*, *>)["refresh_token"] as List<*>).first())
        assertEquals("refresh_token", ((httpEntityCaptor.firstValue.body as Map<*, *>)["grant_type"] as List<*>).first())
        assertEquals(MediaType.APPLICATION_FORM_URLENCODED, httpEntityCaptor.firstValue.headers.contentType)

        val encodedPass = Base64.getEncoder().encode("$clientId:$clientSecret".toByteArray())
        assertEquals("Basic ${String(encodedPass)}", (httpEntityCaptor.firstValue.headers["Authorization"] as List<*>).first())
    }

    @Test
    fun `register success`() {

        val name = "Emil"
        val pass = "Password"
        val token = Token("access token", "barer", "refresh token", 534L, "scope", Date().time)
        val urlTokenCaptor = argumentCaptor<String>()
        val httpEntityTokenCaptor = argumentCaptor<HttpEntity<*>>()
        val tokenResponseEntity = ResponseEntity(token, HttpStatus.OK)
        doReturn(tokenResponseEntity).`when`(restTemplate).postForEntity(urlTokenCaptor.capture(), httpEntityTokenCaptor.capture(), eq(Token::class.java))

        val registerResponseEntity = ResponseEntity("", HttpStatus.OK)
        val urlRegisterCaptor = argumentCaptor<String>()
        val httpEntityRegisterCaptor = argumentCaptor<HttpEntity<*>>()
        doReturn(registerResponseEntity).`when`(restTemplate).postForEntity(urlRegisterCaptor.capture(), httpEntityRegisterCaptor.capture(), eq(String::class.java))

        val result = loginService.register(name, pass)

        verify(restTemplate, Times(1)).postForEntity(ArgumentMatchers.any<String>(), ArgumentMatchers.any(), eq(Token::class.java))
        verify(restTemplate, Times(1)).postForEntity(ArgumentMatchers.any<String>(), ArgumentMatchers.any(), eq(String::class.java))

        assertEquals(200, result)
        assertEquals("$authServerUrl$tokenMapping", urlTokenCaptor.firstValue)
        assertEquals(clientId, ((httpEntityTokenCaptor.firstValue.body as Map<*, *>)["client_id"] as List<*>).first())
        assertEquals(clientSecret, ((httpEntityTokenCaptor.firstValue.body as Map<*, *>)["client_secret"] as List<*>).first())
        assertEquals("client_credentials", ((httpEntityTokenCaptor.firstValue.body as Map<*, *>)["grant_type"] as List<*>).first())
        assertEquals(MediaType.APPLICATION_FORM_URLENCODED, httpEntityTokenCaptor.firstValue.headers.contentType)
        val encodedPass = Base64.getEncoder().encode("$clientId:$clientSecret".toByteArray())
        assertEquals("Basic ${String(encodedPass)}", (httpEntityTokenCaptor.firstValue.headers["Authorization"] as List<*>).first())

        assertEquals("$authServerUrl$userMapping", urlRegisterCaptor.firstValue)
        assertEquals(MediaType.APPLICATION_JSON, httpEntityRegisterCaptor.firstValue.headers.contentType)
        assertEquals("${token.tokenType} ${token.accessToken}", (httpEntityRegisterCaptor.firstValue.headers["Authorization"] as List<*>).first())
        val body = httpEntityRegisterCaptor.firstValue.body as String
        val jsonBody = JsonParser().parse(body) as JsonObject
        assertTrue(jsonBody.has("username"))
        assertEquals(name, jsonBody["username"].asString)
        assertTrue(jsonBody.has("password"))
        assertEquals(pass, jsonBody["password"].asString)
        assertTrue(jsonBody.has("roles"))
        assertEquals(1, jsonBody["roles"].asJsonArray.size())
        assertEquals("ROLE_USER", jsonBody["roles"].asJsonArray.first().asString)
    }

    @Test
    fun `register can not get token`() {

        val urlCaptor = argumentCaptor<String>()
        val httpEntityCaptor = argumentCaptor<HttpEntity<*>>()
        doThrow(RestClientException::class.java).`when`(restTemplate).postForEntity(urlCaptor.capture(), httpEntityCaptor.capture(), eq(Token::class.java))


        val result = loginService.register("Emil", "12345678")

        verify(restTemplate, Times(1)).postForEntity(ArgumentMatchers.any<String>(), ArgumentMatchers.any(), eq(Token::class.java))
        verify(restTemplate, Times(0)).postForEntity(ArgumentMatchers.any<String>(), ArgumentMatchers.any(), eq(String::class.java))

        assertEquals("$authServerUrl$tokenMapping", urlCaptor.firstValue)
        assertEquals(500, result)
        assertEquals(clientId, ((httpEntityCaptor.firstValue.body as Map<*, *>)["client_id"] as List<*>).first())
        assertEquals(clientSecret, ((httpEntityCaptor.firstValue.body as Map<*, *>)["client_secret"] as List<*>).first())
        assertEquals("client_credentials", ((httpEntityCaptor.firstValue.body as Map<*, *>)["grant_type"] as List<*>).first())
        assertEquals(MediaType.APPLICATION_FORM_URLENCODED, httpEntityCaptor.firstValue.headers.contentType)

        val encodedPass = Base64.getEncoder().encode("$clientId:$clientSecret".toByteArray())
        assertEquals("Basic ${String(encodedPass)}", (httpEntityCaptor.firstValue.headers["Authorization"] as List<*>).first())
    }

    @Test
    fun `register send request throw exception`() {

        val token = Token("access token", "barer", "refresh token", 534L, "scope", Date().time)
        val tokenResponseEntity = ResponseEntity(token, HttpStatus.OK)
        doReturn(tokenResponseEntity).`when`(restTemplate).postForEntity(com.nhaarman.mockitokotlin2.any<String>(), com.nhaarman.mockitokotlin2.any<HttpEntity<*>>(), eq(Token::class.java))
        doThrow(HttpClientErrorException(HttpStatus.FORBIDDEN)).`when`(restTemplate).postForEntity(com.nhaarman.mockitokotlin2.any<String>(), com.nhaarman.mockitokotlin2.any<HttpEntity<*>>(), eq(String::class.java))

        val result = loginService.register("Emil", "Password")

        verify(restTemplate, Times(1)).postForEntity(ArgumentMatchers.any<String>(), ArgumentMatchers.any(), eq(Token::class.java))
        verify(restTemplate, Times(1)).postForEntity(ArgumentMatchers.any<String>(), ArgumentMatchers.any(), eq(String::class.java))

        assertEquals(403, result)
    }
}