package com.example.turistguidedel2.controller;

import com.example.turistguidedel2.model.Attraction;
import com.example.turistguidedel2.model.Tags;
import com.example.turistguidedel2.repository.AttractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/attractions")
public class AttractionController {

    private final AttractionRepository attractionRepository;

    @Autowired
    public AttractionController(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }

    @GetMapping()
    public String showAttractions(Model model) {
        model.addAttribute("attractions", attractionRepository.getAllAttractions());
        return "attractionList";
    }

    @GetMapping("/{name}/tags")
    public String getTagsForAttraction(@PathVariable String name, Model model) {
        List<Tags> tags = attractionRepository.getTagsByName(name);
        model.addAttribute("tags", tags);
        model.addAttribute("name", name);
        return "attractionTags";
    }

    @GetMapping("/add")
    public String showAddAttractionForm(Model model) {
        model.addAttribute("attraction", new Attraction());
        List<Tags> tags = attractionRepository.getTags();
        model.addAttribute("tags",tags);
        return "addAttractionForm";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Attraction attraction) {
        System.out.println("Attraction: " + attraction);
        System.out.println(attraction.getTags().size());
        System.out.println(attraction.getTags());
        attractionRepository.addAttraction(attraction);
        return "redirect:/attractions";
    }

    @GetMapping("/{name}/edit")
    public String editAttraction(@PathVariable String name,Model model) {
        Attraction attraction = attractionRepository.getAttraction(name);
        model.addAttribute("attraction", attraction);
        List<Tags> tags = attractionRepository.getTags();
        model.addAttribute("tags", tags);
        return "editAttractionForm";
    }

    @PostMapping("/update")
    public String updateAttraction(@ModelAttribute Attraction attraction) {
        attractionRepository.updateAttraction(attraction);
        return "redirect:/attractions";
    }

    @GetMapping("/{name}/delete")
    public String deleteAttraction(@PathVariable String name) {
        attractionRepository.deleteAttraction(name);
        return "redirect:/attractions";
    }


}