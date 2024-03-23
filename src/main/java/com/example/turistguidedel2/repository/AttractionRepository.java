package com.example.turistguidedel2.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.example.turistguidedel2.model.Attraction;
import com.example.turistguidedel2.model.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AttractionRepository {
    @Value("${spring.datasource.url}")
    private String db_url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String pass;

    @Autowired
    private DataSource dataSource;



    public List<Attraction> getAllAttractions() {
        String sql = "SELECT * FROM attraction";
        List<Attraction> attractions = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Attraction attraction = new Attraction();
                attraction.setId(resultSet.getInt("attraction_id"));
                attraction.setName(resultSet.getString("name"));
                attraction.setDescription(resultSet.getString("description"));
                attraction.setCity(resultSet.getString("city"));
                attractions.add(attraction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attractions;
    }

    public Attraction getAttraction(String name) {
        String sql = "SELECT * FROM attraction WHERE name = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Attraction attraction = new Attraction();
                    attraction.setId(resultSet.getInt("attraction_id"));
                    attraction.setName(resultSet.getString("name"));
                    attraction.setDescription(resultSet.getString("description"));
                    attraction.setCity(resultSet.getString("city"));
                    return attraction;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addAttraction(Attraction attraction) {
        String sql = "INSERT INTO attraction (name, description, city) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, attraction.getName());
            statement.setString(2, attraction.getDescription());
            statement.setString(3, attraction.getCity());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Attraction updateAttraction(Attraction attraction) {
        String sql = "UPDATE attraction SET description = ?, city = ? WHERE name = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, attraction.getDescription());
            statement.setString(2, attraction.getCity());
            statement.setString(3, attraction.getName());
            statement.executeUpdate();
            return attraction;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Attraction deleteAttraction(String name) {
        Attraction attraction = getAttraction(name);
        if (attraction != null) {
            String sql = "DELETE FROM attraction WHERE name = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return attraction;
    }

    public List<Tags> getTagsByName(String name) {
        String sql = "SELECT t.name FROM tag t INNER JOIN attraction_tags at ON t.tag_id = at.tag_id " +
                "INNER JOIN attraction a ON at.attraction_id = a.attraction_id WHERE a.name = ?";
        List<Tags> tags = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String tagName = resultSet.getString("name");
                    Tags tag = Tags.valueOf(tagName.toUpperCase());
                    tags.add(tag);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    public List<Tags> getTags() {
        String sql = "SELECT * FROM tag";
        List<Tags> tags = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String tagName = resultSet.getString("name");
                Tags tag = Tags.valueOf(tagName.toUpperCase());
                tags.add(tag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }
}