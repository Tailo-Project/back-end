package com.growith.tailo.common.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${RABBIT_HOST}")
    private String host;

    @Value("${RABBIT_USERNAME}")
    private String username;

    @Value("${RABBIT_PASSWORD}")
    private String password;

    @Value("${spring.rabbitmq.chat.exchange}")
    private String chatExchange;
    @Value("${spring.rabbitmq.chat.routing-key}")
    private String chatRouting;
    @Value("${spring.rabbitmq.chat.queue}")
    private String chatQueue;

    @Value("${spring.rabbitmq.notification.exchange}")
    private String notificationExchange;
    @Value("${spring.rabbitmq.notification.routing-key}")
    private String notificationRouting;
    @Value("${spring.rabbitmq.notification.queue}")
    private String notificationQueue;

    // 공통 메서드로 Exchange, Queue, Binding 설정
    private TopicExchange createTopicExchange(String exchangeName) {
        return new TopicExchange(exchangeName);
    }

    private Queue createQueue(String queueName) {
        return new Queue(queueName);
    }

    private Binding createBinding(Queue queue, TopicExchange exchange, String routingKey) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(routingKey + "*");
    }

    // 채팅 관련 Bean 설정
    @Bean
    public TopicExchange chatExchange() {
        return createTopicExchange(chatExchange);
    }

    @Bean
    public Queue chatQueue() {
        return createQueue(chatQueue);
    }

    @Bean
    public Binding chatBinding() {
        return createBinding(chatQueue(), chatExchange(), chatRouting);
    }

    // 알림 관련 Bean 설정
    @Bean
    public TopicExchange notifyExchange() {
        return createTopicExchange(notificationExchange);
    }

    @Bean
    public Queue notifyQueue() {
        return createQueue(notificationQueue);
    }

    @Bean
    public Binding notifyBinding() {
        return createBinding(notifyQueue(), notifyExchange(), notificationRouting);
    }

    // admin
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
    // 메시지 직렬화 및 역 직렬화
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    // 실제 레빗엠큐 적용 템플릿
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    //ConnectionFactory 등록
    //ConnectionFactory는 RabbitMQ 서버에 접근하기 위한 클래스입니다.
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }
}