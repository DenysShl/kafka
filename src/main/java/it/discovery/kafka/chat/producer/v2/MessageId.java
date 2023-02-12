package it.discovery.kafka.chat.producer.v2;

public record MessageId(int partition, int offset) {
}
