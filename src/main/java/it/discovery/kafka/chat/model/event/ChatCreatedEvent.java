package it.discovery.kafka.chat.model.event;

public record ChatCreatedEvent(String name, String owner) {
}
