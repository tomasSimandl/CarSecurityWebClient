package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Token
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import java.util.*


@Service
class UserServiceImpl(

        @Value("\${auth.server.url}")
        private val authorizationServerUrl: String,

        @Value("\${auth.client.id}")
        private val clientId: String,

        @Value("\${auth.client.secret}")
        private val clientSecret: String,


        @Qualifier("loginRestTemplate")
        private val restTemplate: RestTemplate
) : UserService {

    private val logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    private val oauthGrantType = "password"

    override fun login(username: String, password: String): Token {

        val header = HttpHeaders()
        header.contentType = MediaType.APPLICATION_FORM_URLENCODED
        header.setBasicAuth(clientId, clientSecret)

        val form = LinkedMultiValueMap<String, String>()
        form["username"] = username
        form["password"] = password
        form["grant_type"] = oauthGrantType

        val httpEntity = HttpEntity(form, header)

        logger.debug("Sending login request to authorization server.")

        val tokenEntity = restTemplate.postForEntity(authorizationServerUrl + TOKEN_MAPPING, httpEntity, Token::class.java)
        return tokenEntity.body!!
    }

    override fun refresh(refreshToken: String): Token {
        val header = HttpHeaders()
        header.contentType = MediaType.APPLICATION_FORM_URLENCODED
        header.setBasicAuth(clientId, clientSecret)

        val form = LinkedMultiValueMap<String, String>()
        form["refresh_token"] = refreshToken
        form["grant_type"] = "refresh_token"

        val httpEntity = HttpEntity(form, header)
        logger.debug("Sending refresh token request to authorization server.")

        val tokenEntity = restTemplate.postForEntity(authorizationServerUrl + TOKEN_MAPPING, httpEntity, Token::class.java)
        return tokenEntity.body!!
    }

    override fun register(username: String, password: String): Int {

        val token = getClientCredentialsToken()
        if(!token.isPresent){
            return 500
        }

        val header = HttpHeaders()
        header.contentType = MediaType.APPLICATION_JSON
        header["Authorization"] = "${token.get().tokenType} ${token.get().accessToken}"

        val json = JsonObject()
        json.addProperty("username", username)
        json.addProperty("password", password)
        val jsonRoles = JsonArray(1)
        jsonRoles.add("ROLE_USER")
        json.add("roles", jsonRoles)


        val httpEntity = HttpEntity(json.toString(), header)
        logger.debug("Sending registration request to authorization server.")

        try {
            val entity = restTemplate.postForEntity(authorizationServerUrl + USER_MAPPING, httpEntity, String::class.java)
            return entity.statusCodeValue

        } catch (e: HttpStatusCodeException){
            logger.debug("Server response with status code: ${e.statusCode}")
            return e.statusCode.value()
        }
    }

    private fun getClientCredentialsToken(): Optional<Token> {
        try {
            val header = HttpHeaders()
            header.contentType = MediaType.APPLICATION_FORM_URLENCODED
            header.setBasicAuth(clientId, clientSecret)

            val form = LinkedMultiValueMap<String, String>()
            form["client_id"] = clientId
            form["client_secret"] = clientSecret
            form["grant_type"] = "client_credentials"

            val httpEntity = HttpEntity(form, header)

            logger.debug("Sending client credentials request to authorization server.")

            val tokenEntity = restTemplate.postForEntity(authorizationServerUrl + TOKEN_MAPPING, httpEntity, Token::class.java)
            return Optional.of(tokenEntity.body!!)

        }catch (e:Exception) {
            logger.error("Can not login in authorization server.", e)
            return Optional.empty()
        }
    }
}