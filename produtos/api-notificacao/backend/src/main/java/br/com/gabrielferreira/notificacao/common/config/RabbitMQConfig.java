package br.com.gabrielferreira.notificacao.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    private final CachingConnectionFactory cachingConnectionFactory;

    private final String exchangePedidoNotificacao;

    private final String routingKeyPedidoNotificacao;

    private final String queuePedidoNotificacao;

    private final String exchangePedidoNotificacaoDlx;

    private final String routingKeyPedidoNotificacaoDlx;

    private final String queuePedidoNotificacaoDlq;

    private final String exchangePedidoNotificacaoPl;

    private final String routingKeyPedidoNotificacaoPl;

    private final String queuePedidoNotificacaoPl;

    public RabbitMQConfig(CachingConnectionFactory cachingConnectionFactory,
                          @Value("${broker.exchange.pedido.notificacao.event}") String exchangePedidoNotificacao,
                          @Value("${broker.key.pedido.notificacao.event}")  String routingKeyPedidoNotificacao,
                          @Value("${broker.queue.pedido.notificacao.event}") String queuePedidoNotificacao,
                          @Value("${broker.exchange.pedido.notificacao.dlx}") String exchangePedidoNotificacaoDlx,
                          @Value("${broker.key.pedido.notificacao.dlx}") String routingKeyPedidoNotificacaoDlx,
                          @Value("${broker.queue.pedido.notificacao.dlq}") String queuePedidoNotificacaoDlq,
                          @Value("${broker.exchange.pedido.notificacao.pl}") String exchangePedidoNotificacaoPl,
                          @Value("${broker.key.pedido.notificacao.pl}") String routingKeyPedidoNotificacaoPl,
                          @Value("${broker.queue.pedido.notificacao.pl}") String queuePedidoNotificacaoPl) {
        this.cachingConnectionFactory = cachingConnectionFactory;
        this.exchangePedidoNotificacao = exchangePedidoNotificacao;
        this.routingKeyPedidoNotificacao = routingKeyPedidoNotificacao;
        this.queuePedidoNotificacao = queuePedidoNotificacao;
        this.exchangePedidoNotificacaoDlx = exchangePedidoNotificacaoDlx;
        this.routingKeyPedidoNotificacaoDlx = routingKeyPedidoNotificacaoDlx;
        this.queuePedidoNotificacaoDlq = queuePedidoNotificacaoDlq;
        this.exchangePedidoNotificacaoPl = exchangePedidoNotificacaoPl;
        this.routingKeyPedidoNotificacaoPl = routingKeyPedidoNotificacaoPl;
        this.queuePedidoNotificacaoPl = queuePedidoNotificacaoPl;
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
    public Queue queuePedidoNotificacaoPl(){
        return new Queue(queuePedidoNotificacaoPl);
    }

    @Bean
    public TopicExchange topicExchangePedidoNotificacaoPl(){
        return new TopicExchange(exchangePedidoNotificacaoPl);
    }

    @Bean
    public Binding bindingPedidoNotificacaoPl(){
        return BindingBuilder
                .bind(queuePedidoNotificacaoPl())
                .to(topicExchangePedidoNotificacaoPl())
                .with(routingKeyPedidoNotificacaoPl);
    }

    @Bean
    public Queue queuePedidoNotificacaoDlq(){
        return new Queue(queuePedidoNotificacaoDlq);
    }

    @Bean
    public TopicExchange topicExchangePedidoNotificacaoDlx(){
        return new TopicExchange(exchangePedidoNotificacaoDlx);
    }

    @Bean
    public Binding bindingPedidoNotificacaoDlx(){
        return BindingBuilder
                .bind(queuePedidoNotificacaoDlq())
                .to(topicExchangePedidoNotificacaoDlx())
                .with(routingKeyPedidoNotificacaoDlx);
    }

    @Bean
    public Queue queuePedidoNotificacao(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", exchangePedidoNotificacaoDlx);
        args.put("x-dead-letter-routing-key", routingKeyPedidoNotificacaoDlx);
        return new Queue(queuePedidoNotificacao, true, false, false, args);
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
