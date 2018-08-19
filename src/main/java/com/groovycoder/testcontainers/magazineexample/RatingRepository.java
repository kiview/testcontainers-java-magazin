package com.groovycoder.testcontainers.magazineexample;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class RatingRepository {

    private final String bootstrapServers;
    private KafkaProducer<String, String> producer;

    public RatingRepository(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    void init() {
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "rating-repository");

        producer = new KafkaProducer<>(props,
                new StringSerializer(),
                new StringSerializer());
    }

    void close() {
        producer.close();
    }

    public void insert(Rating rating) throws ExecutionException, InterruptedException {
        producer.send(new ProducerRecord<>("ratings", rating.getTalkId().toString())).get();
    }
}
