package com.vce.vce._shared.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
   
   @Bean
   public ObjectMapper objectMapper() {
       return new ObjectMapper()
           .registerModule(new JavaTimeModule())
           .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
           .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
           .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
           .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
   }
}