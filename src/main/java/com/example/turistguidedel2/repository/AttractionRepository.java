package com.example.turistguidedel2.repository;

import com.example.turistguidedel2.model.Attraction;
import com.example.turistguidedel2.model.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AttractionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Attraction> getAllAttractions() {
        String sql = "SELECT * FROM attraction";
        List<Attraction> attractions = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> row : rows) {
            Attraction attraction = new Attraction();
            attraction.setId((Integer) row.get("attraction_id"));
            attraction.setName((String) row.get("name"));
            attraction.setDescription((String) row.get("description"));
            attraction.setCity((String) row.get("city"));
            attractions.add(attraction);
        }
        return attractions;
    }

    public Attraction getAttraction(String name) {
        String sql = "SELECT * FROM attraction WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{name}, (rs, rowNum) -> {
            Attraction attraction = new Attraction();
            attraction.setId(rs.getInt("attraction_id"));
            attraction.setName(rs.getString("name"));
            attraction.setDescription(rs.getString("description"));
            attraction.setCity(rs.getString("city"));
            return attraction;
        });
    }

    public void addAttraction(Attraction attraction) {
        String sql = "INSERT INTO attraction (name, description, city) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, attraction.getName(), attraction.getDescription(), attraction.getCity());
    }

    public Attraction updateAttraction(Attraction attraction) {
        String sql = "UPDATE attraction SET description = ?, city = ? WHERE name = ?";
        jdbcTemplate.update(sql, attraction.getDescription(), attraction.getCity(), attraction.getName());
        return attraction;
    }

    public Attraction deleteAttraction(String name) {
        Attraction attraction = getAttraction(name);
        String sql = "DELETE FROM attraction WHERE name = ?";
        jdbcTemplate.update(sql, name);
        return attraction;
    }

    public List<Tags> getTagsByName(String name) {
        String sql = "SELECT t.name FROM tag t INNER JOIN attraction_tags at ON t.tag_id = at.tag_id " +
                "INNER JOIN attraction a ON at.attraction_id = a.attraction_id WHERE a.name = ?";
        List<Tags> tags = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, name);
        for (Map<String, Object> row : rows) {
            String tagName = (String) row.get("name");
            // Assuming Tags enum has a valueOf method to convert string to enum
            Tags tag = Tags.valueOf(tagName.toUpperCase());
            tags.add(tag);
        }
        return tags;
    }

    public List<Tags> getTags() {
        String sql = "SELECT * FROM tag";
        List<Tags> tags = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> row : rows) {
            String tagName = (String) row.get("name");
            // Assuming Tags enum has a valueOf method to convert string to enum
            Tags tag = Tags.valueOf(tagName.toUpperCase());
            tags.add(tag);
        }
        return tags;
    }
}