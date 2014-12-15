package bynull.realty.im.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */
@Configuration
@ComponentScan("bynull.realty.im")
@PropertySource(value = "classpath:im.properties", ignoreResourceNotFound = true)
public class ImApplicationConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
