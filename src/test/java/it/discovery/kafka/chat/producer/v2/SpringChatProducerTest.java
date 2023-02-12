package it.discovery.kafka.chat.producer.v2;

import it.discovery.kafka.chat.ChatApplication;
import it.discovery.kafka.chat.model.ChatMessageVO;
import it.discovery.kafka.chat.model.MessageType;
import it.discovery.kafka.chat.producer.BaseKafkaTest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ChatApplication.class)
class SpringChatProducerTest extends BaseKafkaTest {
    @Autowired
    ChatProducer<SendResult<String, ChatMessageVO>> chatProducer;

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.producer.bootstrap-servers[0]", () -> kafka.getBootstrapServers());
        registry.add("spring.kafka.consumer.bootstrap-servers[0]", () -> kafka.getBootstrapServers());
        registry.add("spring.kafka.consumer.properties.spring.json.trusted.packages", () -> "*");
    }

    @Test
        //TODO fix not trusted error
    void send_messageValid_success() throws ExecutionException, InterruptedException {
        ChatMessage message = new ChatMessage("Kevin", "Hello!", "IT-Events", MessageType.TEXT);
        Future<SendResult<String, ChatMessageVO>> future = chatProducer.send(message);
        future.get();
        ConsumerRecords<String, ChatMessageVO> records =
                readMessages("chat_messages.v2", JsonDeserializer.class.getName());
        Iterator<ConsumerRecord<String, ChatMessageVO>> iterator = records.iterator();
        assertTrue(iterator.hasNext());
        ConsumerRecord<String, ChatMessageVO> record = iterator.next();
        assertEquals(message.sender(), record.key());
        ChatMessageVO value = record.value();
        assertEquals(message.text(), value.text());
        assertEquals(message.chat(), value.chat());
    }
}