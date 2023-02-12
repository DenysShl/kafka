package it.discovery.kafka.chat.model;

public record ChatMessageVO(String text, String chat, MessageType messageType) {
}
