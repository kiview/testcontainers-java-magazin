package com.groovycoder.testcontainers.magazineexample;

import java.sql.*;

public class TalkRepository {

    private final Connection connection;

    private static final String INSERT_STATEMENT = "INSERT INTO talks (title, speaker) VALUES (?, ?)";
    private static final String COUNT_QUERY = "SELECT COUNT (*) FROM talks";

    public TalkRepository(Connection connection) {
        this.connection = connection;
    }

    public void add(Talk talk) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_STATEMENT)) {
            ps.setString(1, talk.getTitle());
            ps.setString(2, talk.getSpeaker());
            ps.executeUpdate();
        }
    }

    public long count() throws SQLException {
        try (Statement s = connection.createStatement()) {
            ResultSet rs = s.executeQuery(COUNT_QUERY);
            rs.next();
            return rs.getLong(1);
        }
    }
}
