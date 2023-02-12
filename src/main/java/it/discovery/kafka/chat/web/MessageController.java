package it.discovery.kafka.chat.web;

import it.discovery.kafka.chat.producer.v2.ChatMessage;
import it.discovery.kafka.chat.producer.v2.ChatProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("messages")
@RequiredArgsConstructor
public class MessageController {

    private final ChatProducer chatProducer;

    @PostMapping
    public void sendMessage(@RequestBody ChatMessageDTO dto) {
        chatProducer.send(new ChatMessage(dto.sender(), dto.text(), dto.chat(), dto.messageType()));
    }
}
