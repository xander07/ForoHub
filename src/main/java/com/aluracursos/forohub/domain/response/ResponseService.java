package com.aluracursos.forohub.domain.response;

import com.aluracursos.forohub.domain.topic.TopicRepository;
import com.aluracursos.forohub.domain.user.UserRepository;
import com.aluracursos.forohub.infra.errors.IntegrityValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResponseRepository repository;

    public ResponseDataT createResponse(ResponseData responseData) {

        // Check if user is registered
        if (!userRepository.findById(responseData.idUser()).isPresent()){
            throw new IntegrityValidation("This user id  ins´t registered on the data base ");
        }
        // Check if topic id exist
        if (!topicRepository.findById(responseData.idTopic()).isPresent()){
            throw new IntegrityValidation("This topic id  ins´t registered on the data base ");
        }
        var user = userRepository.findById(responseData.idUser()).get();
        var topic =topicRepository.findById(responseData.idTopic()).get();

        var rVerified= new Response(null,responseData.solution(),user,topic,responseData.creationdate());
            repository.save(rVerified);
        return  new ResponseDataT(rVerified);
    }

}
