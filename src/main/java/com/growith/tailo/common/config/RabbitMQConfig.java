package com.growith.tailo.common.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${RABBIT_HOST}")
    private String host;

    @Value("${RABBIT_PORT}")
    private int port;

    @Value("${RABBIT_USERNAME}")
    private String username;

    @Value("${RABBIT_PASSWORD}")
    private String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();  // JSON 직렬화/역직렬화 설정
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);  // JSON 변환기를 설정
        return template;
    }

    // 큐와 Exchange 이름 정의
    public static final String DM_EXCHANGE = "directExchange";
    public static final String NOTIFICATION_EXCHANGE = "notificationExchange";

    // 큐 이름
    public static final String DM_QUEUE = "directQueue";
    public static final String NOTIFICATION_QUEUE = "notificationQueue";

    // 1. DM 큐와 Exchange 생성
    @Bean
    public Queue directQueue() {
        return new Queue(DM_QUEUE, true); // 큐 이름과 지속성 설정
    }

    @Bean
    public TopicExchange directExchange() {
        return new TopicExchange(DM_EXCHANGE, true, true); // Exchange 이름, 지속성, 내구성 설정
    }

    @Bean
    public Binding vacationBinding(Queue directQueue, TopicExchange directExchange) {
        return BindingBuilder.bind(directQueue)
                .to(directExchange)
                .with("dm.#"); // 라우팅 키 예: dm.request, dm.approve dm.request.{accountId}, dm.response.{accountId} , .. 등
    }

    // 2. 알림 큐와 Exchange 생성
    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE, true, true);
    }

    @Bean
    public Binding costBinding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(notificationExchange)
                .with("alert.#"); // 라우팅 키 예: alert.dm.{accountId}, alert.like.{accountId},...등 특정 사용자에게 전송 가능
    }

}