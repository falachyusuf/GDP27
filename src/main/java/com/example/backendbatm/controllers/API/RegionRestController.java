package com.example.backendbatm.controllers.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.DTO.RegionDTO;
import com.example.backendbatm.model.Region;
import com.example.backendbatm.repository.RegionRepository;

@RestController
@RequestMapping("api")
public class RegionRestController {
    @Autowired
    private RegionRepository regionRepository;

    @GetMapping("regions")
    public List<Region> getAll() {
        return regionRepository.findAll();
    }

    @GetMapping("regions/{id}")
    public Region getById(@PathVariable(required = true) Integer id) {
        return regionRepository.findById(id).orElse(null);
    }

    @PostMapping("regions")
    public boolean save(@RequestBody Region region) {
        Region result = regionRepository.save(region);
        return regionRepository.findById(result.getId()).isPresent();
    }

    @PatchMapping("regions/{id}")
    public boolean updateById(@PathVariable(required = true) Integer id, @RequestBody RegionDTO regionDTO) {
        Region regionById = regionRepository.findById(id).orElse(null);

        try {
            if (!regionDTO.getName().equals(null) && regionDTO.getName().length() != 0) {
                regionById.setName(regionDTO.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Region updateRegion = regionRepository.save(regionById);
        return regionRepository.findById(updateRegion.getId()).isPresent();
    }

    @DeleteMapping("regions/{id}")
    public boolean deleteById(@PathVariable(required = true) Integer id) {
        regionRepository.deleteById(id);
        return regionRepository.findById(id).isEmpty();
    }
}
