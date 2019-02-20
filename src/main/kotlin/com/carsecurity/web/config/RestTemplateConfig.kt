package com.carsecurity.web.config

import com.carsecurity.web.rest.model.Token
import com.carsecurity.web.rest.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*
import javax.servlet.http.HttpSession

/**
 * Class configures RestTemplate
 */
@Configuration
class LoginRestTemplateConfig {
    @Bean
    fun loginRestTemplate(): RestTemplate = RestTemplate()
}

/**
 * Class configures RestTemplate with authorization header for token communication.
 */
@Configuration
class TokenRestTemplateConfig(private val userService: UserService) {

    @Bean
    fun tokenRestTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        val interceptor = HeaderRequestInterceptor(userService)
        restTemplate.interceptors = listOf(interceptor)

        return restTemplate
    }

    /**
     * Class is used for adding authorization header to every request. Header is added only when token is stored
     * from session.
     */
    private class HeaderRequestInterceptor(
            private val userService: UserService
    ) : ClientHttpRequestInterceptor {

        private val logger = LoggerFactory.getLogger(HeaderRequestInterceptor::class.java)
        private val tokenValidLatency: Long = 60000 // 1 minute

        /**
         * Add authorization header to every request but only when token is stored in session.
         */
        override fun intercept(httpRequest: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {

            val token = getToken()

            if (token != null) {
                httpRequest.headers.set("Authorization", "${token.tokenType} ${token.accessToken}")
            }

            return execution.execute(httpRequest, body)
        }

        /**
         * Return token from session. When token is expired refreshToken method is called. When token is not accessible
         * or when error occurs null is returned.
         */
        private fun getToken(): Token? {
            val attributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
            val httpSession = attributes.request.session
            val token = httpSession?.getAttribute("token")

            if (token != null && token is Token) {

                val tokenExpiresAt = token.incomeTime + (token.expiresIn * 1000) - tokenValidLatency
                if (Date().time >= tokenExpiresAt) {
                    return refreshToken(httpSession, token)
                }
                return token
            }
            return null
        }

        /**
         * Refresh actual token, store it to session and return it. On error remove actual token from session and
         * return null.
         */
        private fun refreshToken(session: HttpSession, token: Token): Token? {

            try {
                logger.debug("Refreshing token.")
                val newToken = userService.refresh(token.refreshToken)
                session.setAttribute("token", newToken)
                return newToken

            } catch (e: HttpStatusCodeException) {
                logger.debug("Can not refresh token. Server response status code: ${e.statusCode} $")
                session.removeAttribute("token")
                return null

            } catch (e: Exception) {
                logger.error("Can not refresh token cause exception", e)
                session.removeAttribute("token")
                return null
            }
        }
    }
}
