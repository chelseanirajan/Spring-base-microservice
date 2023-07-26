package edu.miu.ApiGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Configuration
public class GlobalFilterConfiguration {
    final Logger logger = LoggerFactory.getLogger(GlobalFilterConfiguration.class);

    @Order(1)
    @Bean
    public GlobalFilter firstPreFilter() {

        return (exchange, chain) -> {
            logger.info("My First global pre-filter is executed...");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("My First global post-filter was executed...");
            }));
        };
    }

    @Order(2)
    @Bean
    public GlobalFilter secondPreFilter() {

        return (exchange, chain) -> {
            logger.info("My Second global pre-filter is executed...");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("My Second global post-filter was executed...");
            }));
        };
    }

    @Order(3)
    @Bean
    public GlobalFilter thirdPreFilter() {

        return (exchange, chain) -> {
            logger.info("My Third global pre-filter is executed...");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("My Third global post-filter was executed...");
            }));
        };
    }
}
