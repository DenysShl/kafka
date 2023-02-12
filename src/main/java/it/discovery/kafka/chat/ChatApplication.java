package it.discovery.kafka.chat;

import it.discovery.kafka.chat.model.ChatMessageVO;
import it.discovery.kafka.chat.producer.v2.ChatProducer;
import it.discovery.kafka.chat.producer.v2.SpringChatProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Bean
    ChatProducer chatProducer(KafkaTemplate<String, ChatMessageVO> kafkaTemplate,
                              Environment env) {
        return new SpringChatProducer(kafkaTemplate, env);
    }
}
