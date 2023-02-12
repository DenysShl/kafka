package it.discovery.kafka.chat.producer.v2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class SpringChatProducerTest {
    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.1"));

    @Autowired
    ChatProducer chatProducer;

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.producer.bootstrap-servers[0]", () -> kafka.getBootstrapServers());
        registry.add("spring.kafka.consumer.bootstrap-servers[0]", () -> kafka.getBootstrapServers());
    }

    @Test
    void send() {
    }
}