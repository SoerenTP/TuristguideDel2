package com.example.turistguidedel2.repository;

import com.example.turistguidedel2.model.Attraction;
import com.example.turistguidedel2.model.Tags;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AttractionRepositoryTest {

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
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        attractionRepository = new AttractionRepository();
        attractionRepository.dataSource = dataSource;
    }

    @Test
    public void testGetAllAttractions() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("attraction_id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("AttractionName");
        when(resultSet.getString("description")).thenReturn("AttractionDescription");
        when(resultSet.getString("city")).thenReturn("AttractionCity");

        List<Attraction> attractions = attractionRepository.getAllAttractions();

        assertEquals(1, attractions.size());
        assertEquals("AttractionName", attractions.get(0).getName());
        assertEquals("AttractionDescription", attractions.get(0).getDescription());
        assertEquals("AttractionCity", attractions.get(0).getCity());
    }

    @Test
    public void testGetAttraction() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("attraction_id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("AttractionName");
        when(resultSet.getString("description")).thenReturn("AttractionDescription");
        when(resultSet.getString("city")).thenReturn("AttractionCity");

        Attraction attraction = attractionRepository.getAttraction("AttractionName");

        assertEquals("AttractionName", attraction.getName());
        assertEquals("AttractionDescription", attraction.getDescription());
        assertEquals("AttractionCity", attraction.getCity());
    }

    @Test
    public void testAddAttraction() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Attraction attraction = new Attraction();
        attraction.setName("NewAttraction");
        attraction.setDescription("NewAttractionDescription");
        attraction.setCity("NewAttractionCity");

        attractionRepository.addAttraction(attraction);

        verify(preparedStatement).setString(1, "NewAttraction");
        verify(preparedStatement).setString(2, "NewAttractionDescription");
        verify(preparedStatement).setString(3, "NewAttractionCity");
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateAttraction() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Attraction attraction = new Attraction();
        attraction.setName("ExistingAttraction");
        attraction.setDescription("UpdatedDescription");
        attraction.setCity("UpdatedCity");

        Attraction updatedAttraction = attractionRepository.updateAttraction(attraction);

        verify(preparedStatement).setString(1, "UpdatedDescription");
        verify(preparedStatement).setString(2, "UpdatedCity");
        verify(preparedStatement).setString(3, "ExistingAttraction");
        verify(preparedStatement).executeUpdate();

        assertEquals(attraction, updatedAttraction);
    }

    @Test
    public void testDeleteAttraction() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("attraction_id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("AttractionToDelete");
        when(resultSet.getString("description")).thenReturn("DescriptionToDelete");
        when(resultSet.getString("city")).thenReturn("CityToDelete");

        Attraction deletedAttraction = attractionRepository.deleteAttraction("AttractionToDelete");

        verify(preparedStatement).setString(1, "AttractionToDelete");
        verify(preparedStatement).executeUpdate();

        assertEquals("AttractionToDelete", deletedAttraction.getName());
        assertEquals("DescriptionToDelete", deletedAttraction.getDescription());
        assertEquals("CityToDelete", deletedAttraction.getCity());
    }

    @Test
    public void testGetTagsByName() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("name")).thenReturn("Tag1").thenReturn("Tag2");

        List<Tags> tags = attractionRepository.getTagsByName("AttractionName");

        assertEquals(2, tags.size());
        assertEquals(Tags.PAID, tags.get(0));
        assertEquals(Tags.FAMILY_FRIENDLY, tags.get(1));
    }

    @Test
    public void testGetTags() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("name")).thenReturn("Tag1").thenReturn("Tag2");

        List<Tags> tags = attractionRepository.getTags();

        assertEquals(2, tags.size());
        assertEquals(Tags.HISTORICAL, tags.get(0));
        assertEquals(Tags.AMUSEMENT_PARK, tags.get(1));
    }
}