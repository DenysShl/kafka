package it.discovery.kafka.chat.streams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Properties;

public class StreamCalculation {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "chat-agent");
        properties.put(StreamsConfig.CLIENT_ID_CONFIG, "chat");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaStreams streams = new KafkaStreams(createTopology(), properties);
        streams.cleanUp();
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private static Topology createTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KTable<Integer, Long> table = builder.stream("chat_messages.v2")
                .selectKey((key, value) -> 1)
                .groupByKey()
                .count();
        table.toStream().to("chat_statistics.v2");
        return builder.build();
    }
}
