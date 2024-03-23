package com.example.turistguidedel2.repository;

import com.example.turistguidedel2.model.Attraction;
import com.example.turistguidedel2.model.Tags;
import com.example.turistguidedel2.repository.AttractionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AttractionRepositoryTest {
/*    @Autowired
    private AttractionRepository repository;

    @BeforeEach
    public void init() {
        repository = new AttractionRepository();

    }

    @Test
    public void getAllTest(){
        List<Attraction> expectedResultList = new ArrayList<>();
        expectedResultList.add(new Attraction("Eiffel Tower", "Iconic tower in Paris", "Paris", List.of(Tags.PAID)));
        expectedResultList.add(new Attraction("Statue of Liberty", "Iconic statue in America", "New York", List.of(Tags.FAMILY_FRIENDLY)));
        int expectedSize = expectedResultList.size();
        String expectedFirstResultName = expectedResultList.get(0).getName();
        String expectedSecondResultName = expectedResultList.get(1).getName();
        List<Attraction> resultList = repository.getAllAttractions();
        assertEquals(expectedSize,resultList.size());
        assertEquals(expectedFirstResultName, resultList.get(0).getName());
        assertEquals(expectedSecondResultName, resultList.get(1).getName());

    }

    @Test
    public void addAttractionTest() {
        Attraction attractionToAdd = new Attraction("Den Lille Havfrue",
                "Greatest attraction in Copenhagen", "København", List.of(Tags.FREE));
        repository.addAttraction(attractionToAdd);
        Attraction resultAttraction = repository.getAttraction("Den Lille Havfrue");
        assertEquals(attractionToAdd.getName(), resultAttraction.getName());

    }

    @Test
    public void updateAttractionTest() {
        String updatedDescription = "Statue of Liberty is paid";
        Attraction attractionToUpdate = repository.getAttraction("Statue of Liberty");
        attractionToUpdate.setDescription(updatedDescription);
        repository.updateAttraction(attractionToUpdate);
        Attraction resultAttraction = repository.getAttraction("Statue of Liberty");
        String resultDescription = resultAttraction.getDescription();
        assertEquals(updatedDescription, resultDescription);
    }

    @Test
    public void deleteAttractionTest() {
        String attractionToDelete = "Statue of Liberty";
        repository.deleteAttraction("Statue of Liberty");
        List<Attraction> result = repository.getAllAttractions();
        int resultSize = result.size();
        Attraction SOL = repository.getAttraction("Statue of Liberty");
        assertNull(SOL);
        assertEquals(1, resultSize);
    }

    @Test
    public void getTagsByNameTest() {
        String nameOfTagsToGet = "Statue of Liberty";
        List<Tags> result = repository.getTagsByName(nameOfTagsToGet);
        int tagsCount = result.size();
        assertEquals(1, tagsCount);
        assertTrue(result.contains(Tags.FAMILY_FRIENDLY));
    }*/
}