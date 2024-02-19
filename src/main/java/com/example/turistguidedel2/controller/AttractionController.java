package com.example.turistguidedel2.controller;

import com.example.turistguidedel2.model.Attraction;
import com.example.turistguidedel2.repository.AttractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class AttractionController {

    private final AttractionRepository attractionRepository;

    @Autowired
    public AttractionController(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }

    @GetMapping("/attractions")
    public String showAttractions(Model model) {
        model.addAttribute("attractions", attractionRepository.getAllAttractions());
        return "attractionList";
    }

    @GetMapping("/tags/{name}")
    public String showAttractionTags(@PathVariable String name, Model model) {
        Attraction attraction = attractionRepository.getAttractionByName(name);
        model.addAttribute("attraction", attraction);
        return "tags";
    }

    @GetMapping("/add")
    public String showAddAttractionForm() {
        return "addAttractionForm";
    }

    @PostMapping("/save")
    public String saveAttraction(@RequestParam String name, @RequestParam String description, @RequestParam String city, @RequestParam List<String> tags) {
        Attraction newAttraction = new Attraction(name, description, city, tags);
        attractionRepository.addAttraction(newAttraction);
        return "redirect:/attractions";
    }

    @GetMapping("/{name}/edit")
    public String showEditAttractionForm(@PathVariable String name, Model model) {
        Attraction attraction = attractionRepository.getAttractionByName(name);
        model.addAttribute("attraction", attraction);
        return "editAttractionForm";
    }

    @PostMapping("/update")
    public String updateAttraction(@ModelAttribute Attraction updatedAttraction, @RequestParam("tags") String tags) {
        List<String> tagList = Arrays.asList(tags.split(","));
        updatedAttraction.setTags(tagList);

        attractionRepository.updateAttraction(updatedAttraction);
        return "redirect:/attractions";
    }

}