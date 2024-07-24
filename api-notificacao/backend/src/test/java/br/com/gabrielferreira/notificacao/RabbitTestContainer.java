package br.com.gabrielferreira.notificacao;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.RabbitMQContainer;

import java.util.concurrent.atomic.AtomicBoolean;

public class RabbitTestContainer implements BeforeAllCallback, AfterAllCallback {

    private static final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public static RabbitMQContainer rabbitMQContainer = null;

    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if(!atomicBoolean.get() && rabbitMQContainer == null){
            rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.13.0-management")
                    .withExposedPorts(5672, 15672);
            rabbitMQContainer.start();

            System.setProperty("spring.rabbitmq.host", rabbitMQContainer.getHost());
            System.setProperty("spring.rabbitmq.port", rabbitMQContainer.getAmqpPort().toString());
            System.setProperty("spring.rabbitmq.username", rabbitMQContainer.getAdminUsername());
            System.setProperty("spring.rabbitmq.password", rabbitMQContainer.getAdminPassword());

            atomicBoolean.set(true);
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        if(atomicBoolean.get() && rabbitMQContainer != null){

            if(rabbitListenerEndpointRegistry != null){
                rabbitListenerEndpointRegistry.stop();
            }

            rabbitMQContainer.stop();
            rabbitMQContainer = null;
            atomicBoolean.set(false);
        }
    }
}
