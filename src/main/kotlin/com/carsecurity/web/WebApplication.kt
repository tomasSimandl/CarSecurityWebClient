package com.carsecurity.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * This class is used for run whole web application.
 */
@SpringBootApplication
class WebApplication

/**
 * Method boot whole web application.
 */
fun main(args: Array<String>) {
    runApplication<WebApplication>(*args)
}
