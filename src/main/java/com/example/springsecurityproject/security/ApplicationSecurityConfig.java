
package com.example.springsecurityproject.security;

import com.example.springsecurityproject.jwt.JwtTokenVerifier;
import com.example.springsecurityproject.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.example.springsecurityproject.repository.TokenRepository;
import com.example.springsecurityproject.repository.UserRepository;
import com.example.springsecurityproject.service.TokenService;
import com.example.springsecurityproject.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

@Slf4j
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) (per averlo abilitato)
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(tokenService,authenticationManager()))
                .addFilterAfter(new JwtTokenVerifier(tokenRepository,userService),JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/register", "/confirm").permitAll()
                .antMatchers("/management/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
                /*
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
    /*
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {

        UserDetails vitoUser = User.builder()
                .username("vito")
                .password(passwordEncoder.encode("password"))
                //.roles(STUDENT.name())
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails ddbdevUser = User.builder()
                .username("ddbdev")
                .password(passwordEncoder.encode("password"))
                //.roles(ADMIN.name())
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails marioUser = User.builder()
                .username("benmar")
                .password(passwordEncoder.encode("password"))
                //.roles(MODERATOR.name())
                .authorities(MODERATOR.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                ddbdevUser,
                vitoUser,
                marioUser
        );
    }
*/
}

