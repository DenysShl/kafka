package it.discovery.kafka.chat.web;

import it.discovery.kafka.chat.model.Chat;
import it.discovery.kafka.chat.model.ChatMessage;
import it.discovery.kafka.chat.producer.ChatProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("messages")
@RequiredArgsConstructor
public class ChatController {

    private final ChatProducer chatProducer;

    private final Environment env;

    @PostMapping
    public void sendMessage(@RequestBody ChatMessageDTO dto) {
        chatProducer.send(new ChatMessage(dto.sender(), dto.text(), new Chat(env.getRequiredProperty("chat.name"))));
    }
}
