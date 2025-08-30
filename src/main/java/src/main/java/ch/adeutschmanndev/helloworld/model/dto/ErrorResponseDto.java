package ch.adeutschmanndev.helloworld.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Error response for API operations")
public class ErrorResponseDto {

    @Schema(description = "Error message", example = "Greeting not found with ID: 123")
    private String message;
}
