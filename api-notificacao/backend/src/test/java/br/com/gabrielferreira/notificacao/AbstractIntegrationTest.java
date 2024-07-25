package br.com.gabrielferreira.notificacao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.RabbitMQContainer;

@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    static RabbitMQContainer RABBIT_MQ_CONTAINER = new RabbitMQContainer("rabbitmq:3.13.0-management")
                .withExposedPorts(5672, 15672);

    @BeforeAll
    static void setUpAll(){
        RABBIT_MQ_CONTAINER.start();
        System.setProperty("spring.rabbitmq.host", RABBIT_MQ_CONTAINER.getHost());
        System.setProperty("spring.rabbitmq.port", RABBIT_MQ_CONTAINER.getAmqpPort().toString());
        System.setProperty("spring.rabbitmq.username", RABBIT_MQ_CONTAINER.getAdminUsername());
        System.setProperty("spring.rabbitmq.password", RABBIT_MQ_CONTAINER.getAdminPassword());
    }

    @AfterAll
    static void setUpDownAll(){
        RABBIT_MQ_CONTAINER.stop();
    }
}
