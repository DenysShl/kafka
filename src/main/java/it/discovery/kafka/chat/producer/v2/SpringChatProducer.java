package it.discovery.kafka.chat.producer.v2;

import it.discovery.kafka.chat.model.ChatMessageVO;
import it.discovery.kafka.chat.model.MessageType;
import it.discovery.kafka.chat.producer.v2.event.ChatCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Future;

@RequiredArgsConstructor
public class SpringChatProducer implements ChatProducer<SendResult<String, ChatMessageVO>> {

    private final KafkaTemplate<String, ChatMessageVO> kafkaTemplate;

    private final KafkaTemplate<String, ChatCreatedEvent> chatTemplate;

    private final Environment env;

    @Override
    @Transactional
    public Future<SendResult<String, ChatMessageVO>> send(ChatMessage message) {
        ProducerRecord<String, ChatMessageVO> record = new ProducerRecord<>(env.getRequiredProperty("topic.messages"),
                message.sender(), new ChatMessageVO(message.text(), message.chat(), message.messageType()));
        return kafkaTemplate.send(record);
    }

    @Override
    //TODO
    public Future<SendResult<String, ChatMessageVO>> like(MessageId id) {
        return null;
    }

    @Override
    @Transactional
    public Future<SendResult<String, ChatMessageVO>> createChat(String name) {
        ProducerRecord<String, ChatCreatedEvent> chatRecord =
                new ProducerRecord<>(env.getRequiredProperty("topic.chats"),
                        name, new ChatCreatedEvent(name));
        chatTemplate.send(chatRecord);

        return send(new ChatMessage("system", name, "Welcome all participants in new chat!", MessageType.TEXT));
    }
}
