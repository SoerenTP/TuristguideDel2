package com.example.turistguidedel2.repository;

import com.example.turistguidedel2.model.Attraction;
import com.example.turistguidedel2.model.Tags;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AttractionRepository {
    @Value("${spring.datasource.url}")
    private String db_url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String pass;

    public List<Attraction> getAllAttractions() {
        List<Attraction> attractions = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(db_url, user, pass)) {
            Statement statement = connection.createStatement();

            String SQL = "SELECT name, description, city, GROUP_CONCAT(tag_name SEPARATOR ', ') AS 'tags' " +
                    "FROM attraction " +
                    "LEFT JOIN attraction_tags ON attraction.attraction_id = attraction_tags.attraction_id " +
                    "LEFT JOIN tag ON attraction_tags.tag_id = tag.tag_id " +
                    "GROUP BY attraction.attraction_id;";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                attractions.add(new Attraction(
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("city"),
                        new ArrayList<>(List.of(
                                resultSet.getString("tags").split(", ")))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return attractions;
    }

    public Attraction getAttraction(String name) {
        Attraction attraction = null;

        try (Connection connection = DriverManager.getConnection(db_url, user, pass)) {
            String SQL = "SELECT name, description, city, GROUP_CONCAT(tag_name SEPARATOR ', ') AS 'tags' " +
                    "FROM attraction " +
                    "LEFT JOIN attraction_tags ON attraction.attraction_id = attraction_tags.attraction_id " +
                    "LEFT JOIN tag ON attraction_tags.tag_id = tag.tag_id " +
                    "WHERE name LIKE ? " +
                    "GROUP BY attraction.attraction_id;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, "%" + name + "%");

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                attraction = new Attraction(
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("city"),
                        new ArrayList<>(List.of(
                                resultSet.getString("tags").split(", ")))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return attraction;
    }

    public void addAttraction(Attraction attraction) {
        try (Connection connection = DriverManager.getConnection(db_url, user, pass)) {
            String SQL = "INSERT INTO attraction(name, description, city) VALUES (?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, attraction.getName());
            ps.setString(2, attraction.getDescription());
            ps.setString(3, attraction.getCity());

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            int attractionId;
            if (generatedKeys.next()) {
                attractionId = generatedKeys.getInt(1);

                for (String tag : attraction.getTags()) {
                    SQL = "INSERT INTO attraction_tags(attraction_id, tag_id) VALUES (?, ?);";
                    ps = connection.prepareStatement(SQL);
                    ps.setInt(1, attractionId);
                    ps.setInt(2, getTagIdFromName(tag));
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Attraction updateAttraction(Attraction attraction) {
        try (Connection connection = DriverManager.getConnection(db_url, user, pass)) {
            String SQL = "UPDATE attraction " +
                    "SET name = ?, description = ?, city = ? " +
                    "WHERE name LIKE ?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, attraction.getName());
            ps.setString(2, attraction.getDescription());
            ps.setString(3, attraction.getCity());
            ps.setString(4, "%" + attraction.getName() + "%");

            ps.executeUpdate();

            // Clear existing tags for this attraction
            SQL = "DELETE FROM attraction_tags WHERE attraction_id IN (SELECT attraction_id FROM attraction WHERE name LIKE ?);";
            ps = connection.prepareStatement(SQL);
            ps.setString(1, "%" + attraction.getName() + "%");
            ps.executeUpdate();

            // Add updated tags
            int attractionId = getAttractionIdFromName(attraction.getName());
            for (String tag : attraction.getTags()) {
                SQL = "INSERT INTO attraction_tags(attraction_id, tag_id) VALUES (?, ?);";
                ps = connection.prepareStatement(SQL);
                ps.setInt(1, attractionId);
                ps.setInt(2, getTagIdFromName(tag));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return attraction;
    }

    public void deleteAttraction(String name) {
        try (Connection connection = DriverManager.getConnection(db_url, user, pass)) {
            String SQL = "DELETE FROM attraction_tags WHERE attraction_id IN (SELECT attraction_id FROM attraction WHERE name LIKE ?);";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, "%" + name + "%");
            ps.executeUpdate();

            SQL = "DELETE FROM attraction WHERE name LIKE ?;";
            ps = connection.prepareStatement(SQL);
            ps.setString(1, "%" + name + "%");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getTags() {
        List<String> tags = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(db_url, user, pass)) {
            Statement statement = connection.createStatement();

            String SQL = "SELECT name FROM tag;";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                tags.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tags;
    }

    // Helper method to get tag id from name
    private int getTagIdFromName(String name) {
        int tagId = 0;

        try (Connection connection = DriverManager.getConnection(db_url, user, pass)) {
            String SQL = "SELECT tag_id FROM tag WHERE name = ?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, name);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                tagId = resultSet.getInt("tag_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tagId;
    }

    // Helper method to get attraction id from name
    private int getAttractionIdFromName(String name) {
        int attractionId = 0;

        try (Connection connection = DriverManager.getConnection(db_url, user, pass)) {
            String SQL = "SELECT attraction_id FROM attraction WHERE name LIKE ?;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, "%" + name + "%");

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                attractionId = resultSet.getInt("attraction_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return attractionId;
    }
}