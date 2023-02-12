package it.discovery.kafka.chat;

import it.discovery.kafka.chat.producer.ChatProducer;
import it.discovery.kafka.chat.producer.SpringChatProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Bean
    ChatProducer chatProducer(KafkaTemplate<String, String> kafkaTemplate) {
        return new SpringChatProducer(kafkaTemplate);
    }
}
