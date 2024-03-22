package com.example.turistguidedel2.repository;

import com.example.turistguidedel2.model.Attraction;
import com.example.turistguidedel2.model.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ComponentScan("com.example.turistguidedel2.repository")
public class AttractionRepositoryTest {

    @Autowired
    private AttractionRepository attractionRepository;

    @Test
    public void testGetAllAttractions() {
        List<Attraction> attractions = attractionRepository.getAllAttractions();
        assertNotNull(attractions);
        assertEquals(2, attractions.size());
    }

    @Test
    public void testGetAttraction() {
        String attractionName = "Eiffel Tower";
        Attraction attraction = attractionRepository.getAttraction(attractionName);
        assertNotNull(attraction);
        assertEquals(attractionName, attraction.getName());
    }

    @Test
    public void testAddAttraction() {
        Attraction newAttraction = new Attraction("Test Attraction", "Test Description", "Test City", List.of(Tags.PAID));
        attractionRepository.addAttraction(newAttraction);
        Attraction retrievedAttraction = attractionRepository.getAttraction("Test Attraction");
        assertNotNull(retrievedAttraction);
        assertEquals("Test Attraction", retrievedAttraction.getName());
    }

    @Test
    public void testUpdateAttraction() {
        String attractionName = "Eiffel Tower";
        Attraction updatedAttraction = new Attraction(attractionName, "Updated Description", "Paris", List.of(Tags.PAID));
        attractionRepository.updateAttraction(updatedAttraction);
        Attraction retrievedAttraction = attractionRepository.getAttraction(attractionName);
        assertNotNull(retrievedAttraction);
        assertEquals("Updated Description", retrievedAttraction.getDescription());
    }

    @Test
    public void testDeleteAttraction() {
        String attractionName = "Statue of Liberty";
        Attraction deletedAttraction = attractionRepository.deleteAttraction(attractionName);
        assertNotNull(deletedAttraction);
        assertEquals(attractionName, deletedAttraction.getName());
        assertNull(attractionRepository.getAttraction(attractionName));
    }

    @Test
    public void testGetTagsByName() {
        String attractionName = "Eiffel Tower";
        List<Tags> tags = attractionRepository.getTagsByName(attractionName);
        assertNotNull(tags);
        assertEquals(1, tags.size()); // Assuming Eiffel Tower has one tag associated
    }

    @Test
    public void testGetTags() {
        List<Tags> tags = attractionRepository.getTags();
        assertNotNull(tags);
        // Assert based on the number of tags in the database
    }
}