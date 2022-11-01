package org.rinatzzak.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.rinatzzak.model.RabbitQueue.*;

@Configuration
public class RabbitConfiguration {

    @Bean
    public MessageConverter jsomMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
