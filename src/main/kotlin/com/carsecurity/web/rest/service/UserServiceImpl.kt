package com.carsecurity.web.rest.service

import com.carsecurity.web.rest.model.Token
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate


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

        println("HEADERS")
        println(httpEntity.headers)
        println("BODY")
        println(httpEntity.body)


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
        println("HEADERS")
        println(httpEntity.headers)
        println("BODY")
        println(httpEntity.body)


        val tokenEntity = restTemplate.postForEntity(authorizationServerUrl + TOKEN_MAPPING, httpEntity, Token::class.java)
        return tokenEntity.body!!
    }
}