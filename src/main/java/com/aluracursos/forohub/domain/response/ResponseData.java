package com.aluracursos.forohub.domain.response;

import com.aluracursos.forohub.domain.topic.Topic;
import com.aluracursos.forohub.domain.user.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ResponseData(
        @NotBlank
        String solution,
        @NotNull
        @Valid
        Long idUser,
        @NotNull
        @Valid
        Long idTopic,
        LocalDateTime creationdate) {
}
