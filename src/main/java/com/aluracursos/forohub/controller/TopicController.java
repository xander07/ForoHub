package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.domain.topic.*;
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
@RequestMapping("/topic")
@SecurityRequirement(name="bearer-key")
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicService topicService;


    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid TopicData topicData) throws IntegrityValidation {
        var finaldata = topicService.createTopic(topicData);
        return ResponseEntity.ok(finaldata);
    }

    @GetMapping
    public ResponseEntity<Page<DataListTopics>>  listOfTopics(@PageableDefault(size = 10) Pageable paged){

        return ResponseEntity.ok(topicRepository.findByActiveTrue(paged).map(DataListTopics::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity updateTopics (@RequestBody @Valid UpdateTopic updateTopic){

        Topic topic= topicRepository.getReferenceById(updateTopic.id());


        topic.updateData(updateTopic);


        return ResponseEntity.ok(new ResponseTopicData(topic.getId(),
                topic.getTitle(),topic.getMessage(),
                topic.getStatus(),topic.getAuthor().getId(),
                topic.getCourse(),topic.getDate()));

    }

    // Endpoint to logically delete a topic
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteTopic(@PathVariable Long id){
        // Retrieve the topic entity by ID for logical deletion
        Topic topic= topicRepository.getReferenceById(id);

        // Perform logical deletion of the topic
        topic.diactivateTopic();

        // Return a 204 No Content response indicating successful deletion
        return ResponseEntity.noContent().build();
    }

    // Endpoint to retrieve topic details by ID
    @GetMapping("/{id}")
    public ResponseEntity <ResponseTopicData> topicReturn(@PathVariable Long id){
        // Retrieve the topic entity by ID to return its details
        Topic topic = topicRepository.getReferenceById(id);

        // Construct and return detailed topic data in the response
        var dTopic = new ResponseTopicData(topic.getId(),
                topic.getTitle(),
                topic.getMessage(),
                topic.getStatus(),
                topic.getAuthor().getId(),
                topic.getCourse(),
                topic.getDate());
        return ResponseEntity.ok(dTopic);
    }

}

