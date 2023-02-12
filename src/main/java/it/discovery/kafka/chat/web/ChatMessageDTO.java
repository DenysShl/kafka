package it.discovery.kafka.chat.web;

import it.discovery.kafka.chat.model.MessageType;

public record ChatMessageDTO(String sender, String text, String chat, MessageType messageType) {
}
