package com.example.turistguidedel2.repository;

import com.example.turistguidedel2.model.Attraction;
import com.example.turistguidedel2.model.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AttractionDBRepository {
    private final DataSource dataSource;

    @Autowired
    public AttractionDBRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Value("${spring.datasource_url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String pass;

    public List<Attraction> getAllAttractions() {
        List<Attraction> attractions = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM attraction");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Attraction attraction = new Attraction();
                attraction.setName(resultSet.getString("name"));
                attraction.setDescription(resultSet.getString("description"));
                attraction.setCity(resultSet.getString("city"));
                attraction.setTags(getTagsForAttraction(attraction.getName()));
                attractions.add(attraction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attractions;
    }

    public Attraction getAttraction(String name) {
        Attraction attraction = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM attraction WHERE name = ?")) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    attraction = new Attraction();
                    attraction.setName(resultSet.getString("name"));
                    attraction.setDescription(resultSet.getString("description"));
                    attraction.setCity(resultSet.getString("city"));
                    attraction.setTags(getTagsForAttraction(attraction.getName()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attraction;
    }

    public void addAttraction(Attraction attraction) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO attraction (name, description, city) VALUES (?, ?, ?)")) {
            statement.setString(1, attraction.getName());
            statement.setString(2, attraction.getDescription());
            statement.setString(3, attraction.getCity());
            statement.executeUpdate();
            addTagsForAttraction(attraction);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAttraction(Attraction attraction) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE attraction SET description = ?, city = ? WHERE name = ?")) {
            statement.setString(1, attraction.getDescription());
            statement.setString(2, attraction.getCity());
            statement.setString(3, attraction.getName());
            statement.executeUpdate();
            deleteTagsForAttraction(attraction.getName());
            addTagsForAttraction(attraction);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAttraction(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM attraction WHERE name = ?")) {
            statement.setString(1, name);
            statement.executeUpdate();
            deleteTagsForAttraction(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Tags> getTagsForAttraction(String attractionName) {
        List<Tags> tags = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT t.name FROM tag t " +
                             "JOIN attraction_tags at ON t.tag_id = at.tag_id " +
                             "JOIN attraction a ON at.attraction_id = a.attraction_id " +
                             "WHERE a.name = ?")) {
            statement.setString(1, attractionName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tags.add(Tags.valueOf(resultSet.getString("name")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    private void addTagsForAttraction(Attraction attraction) {
        try (Connection connection = dataSource.getConnection()) {
            for (Tags tag : attraction.getTags()) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO attraction_tags (attraction_id, tag_id) " +
                                "VALUES ((SELECT attraction_id FROM attraction WHERE name = ?), " +
                                "(SELECT tag_id FROM tag WHERE name = ?))")) {
                    statement.setString(1, attraction.getName());
                    statement.setString(2, tag.name());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTagsForAttraction(String attractionName) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM attraction_tags WHERE attraction_id = " +
                             "(SELECT attraction_id FROM attraction WHERE name = ?)")) {
            statement.setString(1, attractionName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}