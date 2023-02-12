package it.discovery.kafka.chat.model.event;

public record ChatDeletedEvent(String name, String reason) {
}
