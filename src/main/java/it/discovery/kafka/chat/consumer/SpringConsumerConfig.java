package it.discovery.kafka.chat.consumer;

import it.discovery.kafka.chat.model.ChatMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;

@Configuration
@EnableKafka
@Slf4j
public class SpringConsumerConfig {

    @KafkaListener(groupId = "spring-client", topics = "${topic.messages}")
    public void readChatMessages(ConsumerRecord<String, ChatMessageVO> record) {
        log.info("New message {} with type {} in the chat {} from {}", record.value().text(),
                record.value().messageType(), record.value().chat(), record.key());
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<?, ?> kafkaTemplate) {
        //TODO adjust error handler to handle SerializationException
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                ((consumerRecord, e) -> new TopicPartition(consumerRecord.topic() + ".errors",
                        consumerRecord.partition())));
        return new DefaultErrorHandler(recoverer);
    }
}
