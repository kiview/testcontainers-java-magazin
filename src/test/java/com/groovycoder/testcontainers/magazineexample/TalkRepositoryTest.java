package com.groovycoder.testcontainers.magazineexample;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class TalkRepositoryTest {

    @Rule
    public PostgreSQLContainer postgres = new PostgreSQLContainer();

    private Connection c;
    private TalkRepository repository;


    @Before
    public void setUp() throws SQLException {
        c = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());

        try (Statement s = c.createStatement()) {
            s.execute("CREATE TABLE talks (" +
                    "title varchar(255)," +
                    "speaker varchar(255)" +
                    ");");
        }

        repository = new TalkRepository(c);
    }

    @After
    public void tearDown() throws SQLException {
        c.close();
    }

    @Test
    public void is_initially_empty() throws SQLException {
        assertEquals(0, repository.count());
    }

    @Test
    public void contains_one_talk_after_inserting_one_talk() throws SQLException {
        Talk t = new Talk("An introduction to Testcontainers", "Kevin Wittek");
        repository.add(t);

        assertEquals(1, repository.count());
    }

}