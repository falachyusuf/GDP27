package com.example.backendbatm.controllers.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.model.Region;
import com.example.backendbatm.repository.RegionRepository;

@RestController
@RequestMapping("api")
public class RegionRestEmployee {
    @Autowired
    private RegionRepository regionRepository;

    @GetMapping("regions")
    private List<Region> getAll() {
        return regionRepository.findAll();
    }

    @GetMapping("regions/{id}")
    private Region getById(@PathVariable(required = true) Integer id) {
        return regionRepository.findById(id).orElse(null);
    }

    @PostMapping("regions")
    private boolean save(@RequestBody Region region) {
        Region result = regionRepository.save(region);
        return regionRepository.findById(result.getId()).isPresent();
    }

    @DeleteMapping("regions/{id}")
    private boolean delete(@PathVariable(required = true) Integer id) {
        regionRepository.deleteById(id);
        return regionRepository.findById(id).isEmpty();
    }
}
