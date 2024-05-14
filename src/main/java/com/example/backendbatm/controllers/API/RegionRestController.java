package com.example.backendbatm.controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.DTO.RegionDTO;
import com.example.backendbatm.handler.CustomResponse;
import com.example.backendbatm.model.Region;
import com.example.backendbatm.repository.RegionRepository;

@RestController
@RequestMapping("api")
public class RegionRestController {
    @Autowired
    private RegionRepository regionRepository;

    @GetMapping("regions")
    public ResponseEntity<Object> getAll() {
        return CustomResponse.generate(HttpStatus.OK, "Data retrieved", regionRepository.findAll());
    }

    @GetMapping("regions/{id}")
    public ResponseEntity<Object> getById(@PathVariable(required = true) Integer id) {
        Region regionById = regionRepository.findById(id).orElse(null);
        try {
            if (!regionById.equals(null)) {
                return CustomResponse.generate(HttpStatus.OK, "Data is exist", regionById);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CustomResponse.generate(HttpStatus.NOT_FOUND, "Data is not exist");
    }

    @PostMapping("regions")
    public ResponseEntity<Object> save(@RequestBody Region region) {
        regionRepository.save(region);

        return CustomResponse.generate(HttpStatus.CREATED, "Data is created");
    }

    @PatchMapping("regions/{id}")
    public ResponseEntity<Object> updateById(@PathVariable(required = true) Integer id,
            @RequestBody RegionDTO regionDTO) {
        Region regionById = regionRepository.findById(id).orElse(null);

        try {
            if (!regionById.equals(null)) {
                if (!regionDTO.getName().equals(null) && regionDTO.getName().length() != 0) {
                    regionById.setName(regionDTO.getName());
                }
                regionRepository.save(regionById);

                return CustomResponse.generate(HttpStatus.OK, "Data is updated");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CustomResponse.generate(HttpStatus.NOT_FOUND, "Data is not exist, update is failed");
    }

    @DeleteMapping("regions/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(required = true) Integer id) {
        try {
            Region regionById = regionRepository.findById(id).orElse(null);
            if (!regionById.equals(null)) {
                regionRepository.deleteById(id);
            }

            return CustomResponse.generate(HttpStatus.OK, "Data is deleted");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CustomResponse.generate(HttpStatus.NOT_FOUND, "Data is not exist, delete is failed");
    }
}
