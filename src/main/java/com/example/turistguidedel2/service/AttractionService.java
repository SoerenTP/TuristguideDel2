package com.example.turistguidedel2.service;

import com.example.turistguidedel2.model.Attraction;
import com.example.turistguidedel2.model.Tags;
import com.example.turistguidedel2.repository.AttractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttractionService {
    private final AttractionRepository attractionRepository;

    @Autowired
    public AttractionService(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }

    public List<Attraction> getAllAttractions() {
        return attractionRepository.getAllAttractions();
    }

    public List<Tags> getTagsByName(String name) {
        return attractionRepository.getTagsByName(name);
    }

    public List<Tags> getTags() {
        return attractionRepository.getTags();
    }

    public Attraction getAttraction(String name) {
        return attractionRepository.getAttraction(name);
    }

    public Attraction updateAttraction(Attraction attraction) {
        return attractionRepository.updateAttraction(attraction);
    }

    public Attraction deleteAttraction(String name) {
        return attractionRepository.deleteAttraction(name);
    }


}