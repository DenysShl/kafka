package it.discovery.kafka.chat.producer;

import it.discovery.kafka.chat.config.CustomPartitioner;
import it.discovery.kafka.chat.model.Chat;
import it.discovery.kafka.chat.model.ChatMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class JavaClientChatProducer implements ChatProducer{

    private final Properties properties;

    public JavaClientChatProducer(String bootstrapServers) {
        properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());


    }
    @Override
    public Future<RecordMetadata> send(ChatMessage message) {
        //Make it Singleton
        try(KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties)) {
            ProducerRecord<String, String> record = new ProducerRecord<>(message.chat().name(),
                    message.sender(), message.text());
            return producer.send(record);
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Chat chat = new Chat("chat_messages");
        ChatMessage message = new ChatMessage("Peter", "Hello from Peter!", chat);
        ChatProducer producer = new JavaClientChatProducer("localhost:9092");
        for(int i = 0; i < 10; i ++) {
            Future<RecordMetadata> future = producer.send(message);
            RecordMetadata recordMetadata = future.get();
            System.out.println("Message offset: " + recordMetadata.offset());
            System.out.println("Message timestamp: " + recordMetadata.timestamp());
            Thread.sleep(1000);
        }
    }
}
