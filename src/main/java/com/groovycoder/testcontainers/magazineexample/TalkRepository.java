package com.groovycoder.testcontainers.magazineexample;

import java.sql.*;
import java.util.UUID;

public class TalkRepository {

    private final Connection connection;

    private static final String INSERT_STATEMENT = "INSERT INTO talks (id, title, speaker) VALUES (?, ?, ?)";
    private static final String COUNT_QUERY = "SELECT COUNT (*) FROM talks";
    private static final String GET_QUERY = "SELECT * FROM talks WHERE id = ?";

    public TalkRepository(Connection connection) {
        this.connection = connection;
    }

    public void add(Talk talk) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_STATEMENT)) {
            ps.setObject(1, talk.getId());
            ps.setString(2, talk.getTitle());
            ps.setString(3, talk.getSpeaker());
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


    public Talk get(UUID uuid) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(GET_QUERY)) {
            ps.setObject(1, uuid);
            ResultSet rs = ps.executeQuery();
            rs.next();

            return new Talk((UUID) rs.getObject(1), rs.getString(2), rs.getString(3));

        }
    }
}
