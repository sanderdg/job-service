package nl.sadego.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfig {

    @Bean
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(5);
    }

}
