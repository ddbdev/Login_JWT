
package com.example.springsecurityproject.security;

import com.example.springsecurityproject.jwt.JwtTokenVerifier;
import com.example.springsecurityproject.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.example.springsecurityproject.repository.TokenRepository;
import com.example.springsecurityproject.repository.UserRepository;
import com.example.springsecurityproject.service.TokenService;
import com.example.springsecurityproject.service.UserPermissionService;
import com.example.springsecurityproject.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

/**
 * This class is going to set the configuration for the security
 */
@Slf4j
@Configuration
@EnableWebSecurity
@AllArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true) enable this if you put @PreAuthorize in controller
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;

    /**
     * The configure method manage all the request sent to the server, if you want more security remove the
     * csrf.disabled() and remove the comment to .csrf().csrfTokenRepository...
     * It's adding a filter for the authentication and a filter after everyrequest that is our JwtTokenVerifier.
     * @param http HttpSecurity
     * @throws Exception Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) (per averlo abilitato)
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(
                        tokenService,
                        authenticationManager()
                        )
                )
                .addFilterAfter(new JwtTokenVerifier(tokenRepository,userService),JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/","/register", "/confirm", "/login").permitAll()
                .antMatchers("/courses/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
                /* This is only for managing the login with a login form, not using at the moment since i'm working with
                Postman


                Form Based Login
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/courses", true)
                    .passwordParameter("password")
                    .usernameParameter("username")
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(31))//default 2 weeks
                    .key("verysecurekey")
                    .rememberMeParameter("remember-me")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");
                */
    }

}

