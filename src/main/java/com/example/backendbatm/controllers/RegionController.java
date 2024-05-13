package com.example.backendbatm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.backendbatm.repository.RegionRepository;
import com.example.backendbatm.model.Region;

@Controller
@RequestMapping("/region")
public class RegionController {
    @Autowired
    RegionRepository regionRepository;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("regions", regionRepository.findAll());
        return "region/index";
    }

    @GetMapping(value = { "form", "form/{id}" })
    public String form(Model model, @PathVariable(required = false) Integer id) {
        if (id != null) {
            model.addAttribute("region", regionRepository.findById(id));
        } else {
            model.addAttribute("region", new Region());
        }
        return "region/form";
    }

    @PostMapping("save")
    public String save(Region region) {
        regionRepository.save(region);
        if (regionRepository.findById(region.getId()).isPresent()) {
            return "redirect:/region";
        } else {
            return "region/form";
        }
    }

    @PostMapping("delete/{id}")
    public String deleteRegion(@PathVariable(required = true) Integer id) {
        regionRepository.deleteById(id);
        Boolean isDeleted = regionRepository.findById(id).isEmpty();
        if (isDeleted) {
            System.out.println("Data deleted");
        } else {
            System.out.println("Failed to delete data");
        }
        return "redirect:/region";
    }

}
