package com.carsecurity.web.config

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomErrorAttributes: DefaultErrorAttributes() {

    private val logger = LoggerFactory.getLogger(CustomErrorAttributes::class.java)
    private val errorMessage = DefaultErrorAttributes::class.java.name + ".MESSAGE"
    private val errorStatusCode = DefaultErrorAttributes::class.java.name + ".STATUS"


    override fun resolveException(request: HttpServletRequest, response: HttpServletResponse, handler: Any?, ex: Exception): ModelAndView? {

        var message = "Can not communicate with server"
        val statusCode: Int

        if(ex is HttpClientErrorException) {
            logger.debug("Error: rest server response status code: ${ex.statusCode} $")

            if (ex.statusCode == HttpStatus.UNAUTHORIZED || ex.statusCode == HttpStatus.FORBIDDEN) {

                statusCode = HttpServletResponse.SC_UNAUTHORIZED
                message = "Not authorized for this operation."

            } else {
                statusCode = ex.statusCode.value()
                try {
                    val json = JsonParser().parse(ex.responseBodyAsString) as JsonObject
                    if(json.has("error")){
                        message = json["error"].toString().trim('"')
                    }
                }catch (e: Exception){
                    logger.debug("Response can not be parsed to JSON.")
                }
            }
        } else {
            logger.error("Controller ends with exception:", ex)
            statusCode = HttpServletResponse.SC_BAD_REQUEST
            message = "Can not communicate with server: ${ex.message}"
        }

        request.setAttribute(errorStatusCode, statusCode)
        request.setAttribute(errorMessage, message)

        return null
    }

    override fun getErrorAttributes(webRequest: WebRequest, includeStackTrace: Boolean): MutableMap<String, Any> {

        // Get data from resolveException method
        var message = webRequest.getAttribute(errorMessage, 0) as String?
        var statusCode = webRequest.getAttribute(errorStatusCode, 0) as Int?

        if(statusCode == null || statusCode == 0) {
            // Get data given by Spring
            statusCode = webRequest.getAttribute("javax.servlet.error.status_code", 0) as Int?
        }

        if(message == null || message.isBlank()) {
            message = if(statusCode != null) {
                HttpStatus.resolve(statusCode).toString()
            } else {
                "Unknown error!"
            }
        }

        if(statusCode == null || statusCode == 0){
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }

        webRequest.setAttribute("javax.servlet.error.status_code", statusCode, 0)

        val map = mutableMapOf<String,Any>()
        map["error"] = message

        return map
    }


}