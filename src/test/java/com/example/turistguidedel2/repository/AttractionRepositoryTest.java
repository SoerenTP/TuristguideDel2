package com.example.turistguidedel2.repository;

import com.example.turistguidedel2.model.Attraction;
import com.example.turistguidedel2.model.Tags;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AttractionRepositoryTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private AttractionRepository attractionRepository;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(dataSource.getConnection()).thenReturn(connection);
        attractionRepository = new AttractionRepository();
    }

    @Test
    void testGetAllAttractions() throws SQLException {
        // Mocking ResultSet
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("attraction_id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Attraction Name");
        when(resultSet.getString("description")).thenReturn("Attraction Description");
        when(resultSet.getString("city")).thenReturn("Attraction City");

        // Calling method under test
        List<Attraction> attractions = attractionRepository.getAllAttractions();

        // Asserting results
        assertEquals(1, attractions.size());
        Attraction attraction = attractions.get(0);
        assertEquals(1, attraction.getId());
        assertEquals("Attraction Name", attraction.getName());
        assertEquals("Attraction Description", attraction.getDescription());
        assertEquals("Attraction City", attraction.getCity());
    }

    @Test
    void testGetAttraction() throws SQLException {
        // Mocking ResultSet
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("attraction_id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Attraction Name");
        when(resultSet.getString("description")).thenReturn("Attraction Description");
        when(resultSet.getString("city")).thenReturn("Attraction City");

        // Calling method under test
        Attraction attraction = attractionRepository.getAttraction("Attraction Name");

        // Asserting results
        assertEquals(1, attraction.getId());
        assertEquals("Attraction Name", attraction.getName());
        assertEquals("Attraction Description", attraction.getDescription());
        assertEquals("Attraction City", attraction.getCity());
    }

    @Test
    void testAddAttraction() throws SQLException {
        // Mocking PreparedStatement
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);

        // Creating attraction
        Attraction attraction = new Attraction();
        attraction.setName("Test Attraction");
        attraction.setDescription("Test Description");
        attraction.setCity("Test City");

        // Calling method under test
        attractionRepository.addAttraction(attraction);

        // Verifying if executeUpdate() is called
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testUpdateAttraction() throws SQLException {
        // Mocking PreparedStatement
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);

        // Creating attraction
        Attraction attraction = new Attraction();
        attraction.setName("Test Attraction");
        attraction.setDescription("Updated Description");
        attraction.setCity("Updated City");

        // Calling method under test
        Attraction updatedAttraction = attractionRepository.updateAttraction(attraction);

        // Verifying if executeUpdate() is called
        verify(preparedStatement, times(1)).executeUpdate();
        // Asserting returned attraction
        assertEquals(attraction, updatedAttraction);
    }

    @Test
    void testDeleteAttraction() throws SQLException {
        // Mocking getAttraction method
        when(attractionRepository.getAttraction("Attraction Name")).thenReturn(new Attraction());

        // Mocking PreparedStatement
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);

        // Calling method under test
        Attraction deletedAttraction = attractionRepository.deleteAttraction("Attraction Name");

        // Verifying if executeUpdate() is called
        verify(preparedStatement, times(1)).executeUpdate();
        // Asserting returned attraction
        assertEquals("Attraction Name", deletedAttraction.getName());
    }

    @Test
    void testGetTagsByName() throws SQLException {
        // Mocking ResultSet
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString("name")).thenReturn("PAID").thenReturn("FAMILY_FRIENDLY");

        // Calling method under test
        List<Tags> tags = attractionRepository.getTagsByName("Test Attraction");

        // Asserting results
        assertEquals(2, tags.size());
        assertEquals(Tags.PAID, tags.get(0));
        assertEquals(Tags.FAMILY_FRIENDLY, tags.get(1));
    }

    @Test
    void testGetTags() throws SQLException {
        // Mocking ResultSet
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString("name")).thenReturn("PAID").thenReturn("FAMILY_FRIENDLY");

        // Calling method under test
        List<Tags> tags = attractionRepository.getTags();

        // Asserting results
        assertEquals(2, tags.size());
        assertEquals(Tags.PAID, tags.get(0));
        assertEquals(Tags.FAMILY_FRIENDLY, tags.get(1));
    }
}