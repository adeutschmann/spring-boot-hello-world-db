package ch.adeutschmanndev.helloworlddb.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Greeting response containing all greeting information")
public class GreetingResponseDto {

    @Schema(description = "Unique identifier of the greeting", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "The greeting message", example = "Hello World!")
    private String message;

    @Schema(description = "The sender of the greeting", example = "alice")
    private String sender;

    @Schema(description = "The recipient of the greeting", example = "bob")
    private String recipient;

    @Schema(description = "When the greeting was created", example = "2025-08-31T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "When the greeting was last updated", example = "2025-08-31T10:15:30")
    private LocalDateTime updatedAt;
}
