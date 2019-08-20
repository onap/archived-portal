package org.onap.portal.configuration;

import org.onap.portal.service.fn.FnUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
       @Autowired
       private FnUserService fnUserService;

       @Override
       protected void configure(AuthenticationManagerBuilder auth) throws Exception {
              auth.userDetailsService(fnUserService)
                      .passwordEncoder(new PasswordEncoder() {
                             @Override
                             public String encode(CharSequence rawPassword) {
                                    return rawPassword.toString();
                             }

                             @Override
                             public boolean matches(CharSequence rawPassword, String encodedPassword) {
                                    return true;
                             }
                      });
       }

       @Override
       protected void configure(HttpSecurity http) throws Exception {
              http
                      .authorizeRequests()
                      .antMatchers("/static/img/**").permitAll()
                      .anyRequest().authenticated()
                      .and()
                      .formLogin()
                      .loginPage("/login")
                      .permitAll()
                      .and()
                      .logout()
                      .permitAll();

              http.csrf().disable();
              http.headers().frameOptions().disable();

       }


}
