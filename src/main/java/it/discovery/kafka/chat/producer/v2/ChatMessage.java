package it.discovery.kafka.chat.producer.v2;

import it.discovery.kafka.chat.model.MessageType;

public record ChatMessage(String sender, String text, String chat, MessageType messageType) {
}
