package com.carsecurity.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@Configuration
@EnableWebSecurity
class SecurityConfig(
        private val authProvider: CustomAuthenticationProvider
) : WebSecurityConfigurerAdapter() {


    override fun configure( auth: AuthenticationManagerBuilder) {

        auth.authenticationProvider(authProvider)
    }


    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/test") // TODO

    }
}