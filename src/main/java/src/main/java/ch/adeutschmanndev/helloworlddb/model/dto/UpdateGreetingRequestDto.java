package ch.adeutschmanndev.helloworlddb.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Request object for updating an existing greeting")
public class UpdateGreetingRequestDto {

    @NotBlank(message = "Message cannot be blank")
    @Size(max = 500, message = "Message cannot exceed 500 characters")
    @Schema(description = "The updated greeting message", example = "Hello Updated World!", required = true)
    private String message;

    @Size(max = 100, message = "Sender cannot exceed 100 characters")
    @Schema(description = "The updated sender of the greeting", example = "alice")
    private String sender;

    @Size(max = 100, message = "Recipient cannot exceed 100 characters")
    @Schema(description = "The updated recipient of the greeting", example = "bob")
    private String recipient;
}
