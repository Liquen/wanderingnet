package org.wanderingnet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.wanderingnet.service.user.WanderingnetAuthenticationProvider;
import org.wanderingnet.service.user.WanderingnetUserDetailService;

/**
 * Created by guillermoblascojimenez on 08/03/16.
 */
@EnableWebSecurity
@Configuration
@Order(99)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WanderingnetUserDetailService wanderingnetUserDetailService;
    @Autowired
    private WanderingnetAuthenticationProvider wanderingnetAuthenticationProvider;

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .authenticationEventPublisher(authenticationEventPublisher())
                .authenticationProvider(wanderingnetAuthenticationProvider)
                .eraseCredentials(true)
                .userDetailsService(wanderingnetUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/signup").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/static/**").permitAll()
                .anyRequest().authenticated()
                .anyRequest().hasAuthority("WEBAPP")
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/explore")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
        ;
    }
}

