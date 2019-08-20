package org.onap.portal.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@ComponentScan("org.springframework.security.samples.mvc")
public class WebMvcConfiguration implements WebMvcConfigurer {

       @Override
       public void addViewControllers(ViewControllerRegistry registry) {
              registry.addViewController("login.html").setViewName("login.html");
              registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
       }
}
