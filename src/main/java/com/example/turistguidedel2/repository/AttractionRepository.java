package com.example.turistguidedel2.repository;

import com.example.turistguidedel2.model.Attraction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AttractionRepository {
    private List<Attraction> attractions;

    public AttractionRepository() {
        attractions = new ArrayList<>();
        List<String> eiffelTowerTags = new ArrayList<>();
        eiffelTowerTags.add("Iconic");
        eiffelTowerTags.add("Tower");
        attractions.add(new Attraction("Eiffel Tower", "Iconic tower in Paris", "Paris", eiffelTowerTags));

        List<String> statueOfLibertyTags = new ArrayList<>();
        statueOfLibertyTags.add("Iconic");
        statueOfLibertyTags.add("Statue");
        attractions.add(new Attraction("Statue of Liberty", "Iconic statue in America", "New York", statueOfLibertyTags));
    }

    public List<Attraction> getAllAttractions() {
        return attractions;
    }

    public Attraction getAttractionByName(String name) {
        for (Attraction attraction : attractions) {
            if (attraction.getName().equals(name)) {
                return attraction;
            }
        }
        return null;
    }

    public void addAttraction(Attraction attraction) {
        attractions.add(attraction);
    }

    public void updateAttraction(Attraction updatedAttraction) {
        for (Attraction attraction : attractions) {
            if (attraction.getName().equals(updatedAttraction.getName())) {
                attraction.setDescription(updatedAttraction.getDescription());
                attraction.setCity(updatedAttraction.getCity());
                attraction.setTags(updatedAttraction.getTags());
                return;
            }
        }
    }
}
