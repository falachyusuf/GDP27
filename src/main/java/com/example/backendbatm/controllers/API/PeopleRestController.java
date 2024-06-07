package com.example.backendbatm.controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backendbatm.handler.CustomResponse;
import com.example.backendbatm.model.People;
import com.example.backendbatm.repository.PeopleRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api")
public class PeopleRestController {
    @Autowired
    private PeopleRepository peopleRepository;

    @GetMapping("catalogue")
    public ResponseEntity<Object> getAll() {
        return CustomResponse.generate(HttpStatus.OK, "Data retrieved Successfuly", peopleRepository.findAll());
    }

    @PostMapping("catalogue")
    public ResponseEntity<Object> postData(@RequestBody People people) {
        try {
            String url = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png";
            people.setImageURL(url);
            People result = peopleRepository.save(people);
            if(result == null) {
                return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Data failed to post");
            }
            return CustomResponse.generate(HttpStatus.OK, "Data successfully saved");
        } catch (Exception e) {
            return CustomResponse.generate(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @PutMapping("catalogue/{id}")
    public ResponseEntity<Object> updateData(@PathVariable(required = true) Integer id, @RequestBody People people){
        try {
            People peopleData = peopleRepository.findById(id).orElse(null);
            if(peopleData == null){
                return CustomResponse.generate(HttpStatus.BAD_REQUEST, "Data not found");
            }

            if(people.getTitle() != null && !people.getTitle().equals("")){
                peopleData.setTitle(people.getTitle());
            }

            peopleRepository.save(peopleData);
            return CustomResponse.generate(HttpStatus.OK, "Data successfully updated");

        } catch (Exception e) {
            return CustomResponse.generate(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @DeleteMapping("catalogue/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(required = true) Integer id){
        try {
            peopleRepository.deleteById(id);
            return CustomResponse.generate(HttpStatus.OK, "Data successfully deleted");
        } catch (Exception e) {
            return CustomResponse.generate(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
}
