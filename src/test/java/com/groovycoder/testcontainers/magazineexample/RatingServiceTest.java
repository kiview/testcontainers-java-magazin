package com.groovycoder.testcontainers.magazineexample;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class RatingServiceTest {

    @ClassRule
    public static PostgreSQLContainer postgres = new PostgreSQLContainer();

    @ClassRule
    public static KafkaContainer kafkaContainer = new KafkaContainer();

    private static RatingService ratingService;
    private static RatingRepository ratingRepository;
    private static Connection c;

    private static final String TALK_UUID = "6716ac1a-a3ec-11e8-98d0-529269fb1459";

    @BeforeClass
    public static void setUp() throws SQLException {
        c = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());

        try (Statement s = c.createStatement()) {
            s.execute("CREATE TABLE talks (" +
                    "id uuid PRIMARY KEY," +
                    "title varchar(255)," +
                    "speaker varchar(255)" +
                    ");");
        }

        TalkRepository talkRepository = new TalkRepository(c);
        Talk t = new Talk(UUID.fromString(TALK_UUID), "An introduction to Testcontainers", "Kevin Wittek");
        talkRepository.add(t);

        ratingRepository = new RatingRepository(kafkaContainer.getBootstrapServers());
        ratingRepository.init();

        ratingService = new RatingService(talkRepository, ratingRepository);
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        c.close();
        ratingRepository.close();
    }

    @Test
    public void rating_for_existing_talk_is_recorded_in_kafka() throws SQLException, ExecutionException, InterruptedException {
        ratingService.recordRating(new Rating(UUID.fromString(TALK_UUID), 42));

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(
                ImmutableMap.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers(),
                        ConsumerConfig.GROUP_ID_CONFIG, "tc-" + UUID.randomUUID(),
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                ),
                new StringDeserializer(),
                new StringDeserializer());
        consumer.subscribe(Collections.singletonList("ratings"));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
        String firstRecord = records.iterator().next().value();

        assertTrue(firstRecord.contains(TALK_UUID));
    }

}