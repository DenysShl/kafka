package it.discovery.kafka.chat.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class ChatClient {

    private final List<String> chats;

    private final Properties properties;

    public ChatClient(String bootstrapServers, List<String> chats) {
        this.chats = Objects.requireNonNull(chats);

        properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, getClass().getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    public void run() {
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(chats);
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                records.forEach(record -> {
                    System.out.println("New message: " + record.value() + " from " +
                            record.key() + " in the chat: " + record.topic());
                });
            }
        }
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient("localhost:9092", List.of("chat_messages"));
        chatClient.run();
    }
}
