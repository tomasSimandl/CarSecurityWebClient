package com.carsecurity.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

/**
 * Class is used for configure security of web application.
 *
 * @param authProvider is custom provider used for process login requests.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
        private val authProvider: CustomAuthenticationProvider
) : WebSecurityConfigurerAdapter() {

    /**
     * Method sets custom authorization logic with [CustomAuthenticationProvider].
     */
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authProvider)
    }


    /**
     * Method sets authorization level to all request. Requests to: /register, /login, /css, /js endpoints is available
     * without authorization. To any other endpoints user must be logged in.
     *
     * @param http HttpSecurity object on which is set authentication privileges.
     */
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()

                .antMatchers("/register*", "/login*", "/css/**", "/js/**")
                .permitAll()

                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .and()
                .csrf().disable()
    }
}