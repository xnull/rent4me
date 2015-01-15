package bynull.realty.web;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.TimeUnit;

/**
 * @author Vyacheslav Petc
 * @since 1/13/15.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("bynull.realty")
@Profile("local")
@ImportResource({
        "file:src/main/webapp/WEB-INF/applicationContext.xml",
        "file:src/main/webapp/WEB-INF/spring-dispatcher-servlet.xml",
        "file:src/main/webapp/WEB-INF/spring-security-config.xml"
})
public class WebAppStarter {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Cfg.class)
                .addCommandLineProperties(false)
                .showBanner(false)
                .web(true)
                .run(args);
    }

    @EnableWebMvc
    @ComponentScan({"bynull.realty"})
    @Configuration
    public static class Cfg {
        @Bean
        public EmbeddedServletContainerFactory servletContainer() {
            TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
            factory.setPort(9000);
            factory.setSessionTimeout(10, TimeUnit.MINUTES);
            return factory;
        }
    }
}
