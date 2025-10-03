package ch.adeutschmanndev.helloworlddb.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Response for greeting count operation")
public class GreetingCountResponseDto {

    @Schema(description = "Total number of greetings", example = "42")
    private long count;
}
