package br.com.gabrielferreira.produtos.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private final CachingConnectionFactory cachingConnectionFactory;

    private final String exchangePedidoNotificacao;

    private final String routingKeyPedidoNotificacao;

    private final String queuePedidoNotificacao;

    public RabbitMQConfig(CachingConnectionFactory cachingConnectionFactory,
                          @Value("${broker.exchange.pedido.notificacao.event}") String exchangePedidoNotificacao,
                          @Value("${broker.key.pedido.notificacao.event}") String routingKeyPedidoNotificacao,
                          @Value("${broker.queue.pedido.notificacao.event}") String queuePedidoNotificacao) {
        this.cachingConnectionFactory = cachingConnectionFactory;
        this.exchangePedidoNotificacao = exchangePedidoNotificacao;
        this.routingKeyPedidoNotificacao = routingKeyPedidoNotificacao;
        this.queuePedidoNotificacao = queuePedidoNotificacao;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public Queue queuePedidoNotificacao(){
        return new Queue(queuePedidoNotificacao);
    }

    @Bean
    public TopicExchange topicExchangePedidoNotificacao(){
        return new TopicExchange(exchangePedidoNotificacao);
    }

    @Bean
    public Binding bindingPedidoNotificacao(){
        return BindingBuilder
                .bind(queuePedidoNotificacao())
                .to(topicExchangePedidoNotificacao())
                .with(routingKeyPedidoNotificacao);
    }
}
