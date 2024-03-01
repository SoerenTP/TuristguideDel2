package com.example.turistguidedel2.controller;

import com.example.turistguidedel2.controller.AttractionController;
import com.example.turistguidedel2.model.Attraction;
import com.example.turistguidedel2.model.Tags;
import com.example.turistguidedel2.repository.AttractionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttractionControllerTest {

    @Mock
    private AttractionRepository attractionRepository;

    @Mock
    private Model model;

    @InjectMocks
    private AttractionController attractionController;

    @Test
    public void testShowAttractions() {
        List<Attraction> attractions = new ArrayList<>();
        when(attractionRepository.getAllAttractions()).thenReturn(attractions);

        String viewName = attractionController.showAttractions(model);

        assertEquals("attractionList", viewName);
        verify(model).addAttribute("attractions", attractions);
    }

    @Test
    public void testGetTagsForAttraction() {
        String attractionName = "Statue of Liberty";
        List<Tags> tags = new ArrayList<>();
        when(attractionRepository.getTagsByName(attractionName)).thenReturn(tags);

        String viewName = attractionController.getTagsForAttraction(attractionName, model);

        assertEquals("attractionTags", viewName);
        verify(model).addAttribute("tags", tags);
        verify(model).addAttribute("name", attractionName);
    }

    @Test
    public void testShowAddAttractionForm() {
        List<Tags> tags = new ArrayList<>();
        when(attractionRepository.getTags()).thenReturn(tags);

        String viewName = attractionController.showAddAttractionForm(model);

        assertEquals("addAttractionForm", viewName);
        verify(model).addAttribute("attraction", new Attraction());
        verify(model).addAttribute("tags", tags);
    }

    @Test
    public void testSave() {
        Attraction attraction = new Attraction();
        String viewName = attractionController.save(attraction);
        assertEquals("redirect:/attractions", viewName);
        verify(attractionRepository).addAttraction(attraction);
    }

    @Test
    public void testEditAttraction() {
        String attractionName = "Statue of Liberty";
        Attraction attraction = new Attraction();
        List<Tags> tags = new ArrayList<>();
        when(attractionRepository.getAttraction(attractionName)).thenReturn(attraction);
        when(attractionRepository.getTags()).thenReturn(tags);

        String viewName = attractionController.editAttraction(attractionName, model);

        assertEquals("editAttractionForm", viewName);
        verify(model).addAttribute("attraction", attraction);
        verify(model).addAttribute("tags", tags);
    }

    @Test
    public void testUpdateAttraction() {
        Attraction attraction = new Attraction();
        String viewName = attractionController.updateAttraction(attraction);
        assertEquals("redirect:/attractions", viewName);
        verify(attractionRepository).updateAttraction(attraction);
    }

    @Test
    public void testDeleteAttraction() {
        String attractionName = "AttractionName";
        String viewName = attractionController.deleteAttraction(attractionName);
        assertEquals("redirect:/attractions", viewName);
        verify(attractionRepository).deleteAttraction(attractionName);
    }
}