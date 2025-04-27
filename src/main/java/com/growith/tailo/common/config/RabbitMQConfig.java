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

    @Value("${spring.rabbitmq.chat-exchange}")
    private String chatExchange;
    @Value("${spring.rabbitmq.chat-routing-key}")
    private String chatRouting;
    @Value("${spring.rabbitmq.chat-queue}")
    private String chatQueue;

    // 채팅
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(chatExchange);
    }
    @Bean
    public Queue chatQueue(){
        return new Queue(chatQueue);
    }
    @Bean
    public Binding chatBinding(Queue chatQueue, TopicExchange chatExchange){
        return BindingBuilder.bind(chatQueue)
                .to(chatExchange)
                .with(chatRouting+"*");
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