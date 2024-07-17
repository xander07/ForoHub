package com.aluracursos.forohub.domain.topic;

import com.aluracursos.forohub.domain.user.UserRepository;
import com.aluracursos.forohub.infra.errors.IntegrityValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseTopicData createTopic(TopicData topicData){

        // Check if user is registered
        if (!userRepository.findById(topicData.idUser()).isPresent()){
            throw new IntegrityValidation("This user id  ins´t registered on the data base ");
        }
        // Check if the title already exists in the database ignoring case
        var title= topicData.title();
        if (title != null && topicRepository.existsByTitleIgnoreCase(title)){
            throw new IntegrityValidation("This title is already present in the database. Please review the existing topic.");

        }
        // Check if the message already exists in the database ignoring case
        String message = topicData.message();
        if (message != null && topicRepository.existsByMessageIgnoreCase(message)){
            throw new IntegrityValidation("This message is already present in the database. Please review the existing topic.");
        }
        // Retrieve user from database
        var user = userRepository.findById(topicData.idUser()).get();

        // Create and save new topic
        var topic1= new Topic(null,title,message,topicData.date(),topicData.status(),user,topicData.course());

        topicRepository.save(topic1);
        // Return response data
        return new ResponseTopicData(topic1);
    }
}
