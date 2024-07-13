package br.com.gabrielferreira.notificacao;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class RabbitMQTestContainer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    GenericContainer<?> rabbitMQContainer = new GenericContainer<>("rabbitmq:3.13.0-management")
            .withExposedPorts(5672)
            .withEnv("RABBITMQ_DEFAULT_USER", "test-notificacao")
            .withEnv("RABBITMQ_DEFAULT_PASS", "test-notificacao")
            .waitingFor(Wait.forLogMessage(".*Server startup complete.*", 1));

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        rabbitMQContainer.start();
        TestPropertyValues.of(
                "spring.rabbitmq.host=" + rabbitMQContainer.getHost(),
                "spring.rabbitmq.port=" + rabbitMQContainer.getFirstMappedPort().toString(),
                "spring.rabbitmq.username=" + "test-notificacao",
                "spring.rabbitmq.password=" + "test-notificacao"
        ).applyTo(applicationContext.getEnvironment());
    }

}
