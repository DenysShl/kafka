package it.discovery.kafka.chat.producer;

import it.discovery.kafka.chat.model.Chat;
import it.discovery.kafka.chat.model.ChatMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class JavaClientChatProducerTest extends BaseKafkaTest {

    ChatProducer<RecordMetadata> chatProducer;

    @BeforeEach
    void setup() {
        chatProducer = new JavaClientChatProducer(kafka.getBootstrapServers());
    }

    @Test
    void send_messageValid_success() throws ExecutionException, InterruptedException {
        Chat chat = new Chat("chat_messages");
        ChatMessage message = new ChatMessage("Peter", "Hello from Peter!", chat);
        Future<RecordMetadata> future = chatProducer.send(message);
        future.get();

        ConsumerRecords<String, String> records = readMessages(chat.name(), StringDeserializer.class.getName());
        Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
        assertTrue(iterator.hasNext());
        ConsumerRecord<String, String> record = iterator.next();
        assertEquals(message.sender(), record.key());
        assertEquals(message.text(), record.value());
        assertEquals(message.chat().name(), record.topic());
    }
}