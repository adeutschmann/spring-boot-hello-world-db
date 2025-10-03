package ch.adeutschmanndev.helloworlddb.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Hello World response containing a greeting message")
public record HelloWorldResponse(
        @Schema(description = "The greeting message", example = "Hello World")
        String message
) {
}
