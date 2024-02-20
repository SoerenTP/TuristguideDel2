package com.example.turistguidedel2.repository;

import com.example.turistguidedel2.model.Attraction;
import com.example.turistguidedel2.model.Tags;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class AttractionRepository {
    private List<Attraction> attractions;

    public AttractionRepository() {
        attractions = new ArrayList<>();
        attractions.add(new Attraction("Eiffel Tower", "Iconic tower in Paris", "Paris",
                List.of(Tags.PAID)));

        attractions.add(new Attraction("Statue of Liberty", "Iconic statue in America", "New York",
                List.of(Tags.FAMILY_FRIENDLY)));
    }

    public List<Attraction> getAllAttractions() {
        return attractions;
    }

    public Attraction getAttraction(String name) {
        for(Attraction attract : attractions) {
            if (attract.getName().equalsIgnoreCase(name)) {
                return attract;
            }
        }
        return null;
    }

    public void addAttraction(Attraction attraction) {
        attractions.add(attraction);
    }

    public Attraction updateAttraction(Attraction attraction) {
        for(Attraction attrac : attractions) {
            if(attrac.getName().equalsIgnoreCase(attraction.getName())) {
                attrac.setCity(attraction.getCity());
                attrac.setDescription(attraction.getDescription());
                attrac.setTags(attraction.getTags());
                return attrac;
            }
        }
        return null;
    }

    public Attraction deleteAttraction(String name) {
        for(Attraction attrac : attractions) {
            if(attrac.getName().equalsIgnoreCase(name)) {
                attractions.remove(attrac);
                return attrac;
            }
        }
        return null;
    }

    public List<Tags> getTagsByName(String name) {
        for(Attraction attraction : attractions) {
            if(attraction.getName().equalsIgnoreCase(name)) {
                return attraction.getTags();
            }
        }
        return null;
    }

    public List<Tags> getTags() {
        return new ArrayList<>(List.of(Tags.values()));
    }
}
