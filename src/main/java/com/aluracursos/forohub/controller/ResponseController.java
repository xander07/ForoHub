package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.domain.response.*;
import com.aluracursos.forohub.domain.topic.TopicRepository;
import com.aluracursos.forohub.domain.user.UserRepository;
import com.aluracursos.forohub.infra.errors.IntegrityValidation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequestMapping("/response")
@SecurityRequirement(name="bearer-key")
public class ResponseController {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private ResponseRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity responseRegister (@RequestBody @Valid ResponseData responseData) throws IntegrityValidation {

        var finaldata = responseService.createResponse(responseData);


        return ResponseEntity.ok(finaldata);
    }

    @GetMapping
    public ResponseEntity<Page<DataListResponses>>  listOfResponses(@PageableDefault(size = 10) Pageable paged){

        return ResponseEntity.ok(repository.findByActiveTrue(paged).map(DataListResponses::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity updateResponse(@RequestBody @Valid UpdateResponseDetails updateResponseDetails){

        Response response=repository.getReferenceById(updateResponseDetails.id());

        response.responseUpdate(updateResponseDetails);

        return ResponseEntity.ok(new ResponseDataT(response.getId(),response.getSolution(),
                response.getAuthor().getId(),
                response.getTopic().getId(),
                response.getCreationdate()));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteResponse(@PathVariable Long id){

        Response response = repository.getReferenceById(id);

        response.diactivateResponse();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity <ResponseDataT> responseReturn(@PathVariable Long id){

        Response response=repository.getReferenceById(id);
        var dResponse = new ResponseDataT(response.getId(),
                response.getSolution(),
                response.getAuthor().getId(),
                response.getTopic().getId(),
                response.getCreationdate());

        return ResponseEntity.ok(dResponse);
    }
}
