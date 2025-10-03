package ch.adeutschmanndev.helloworlddb.resource;

import ch.adeutschmanndev.helloworlddb.model.dto.HelloWorldResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
@Tag(name = "Hello World Handler", description = "Handler for Hello World operations")
public class HelloWorldHandler {

    @Operation(
            summary = "Handle Hello World request",
            description = "Processes the Hello World request and returns a greeting message"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully returned greeting message",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = HelloWorldResponse.class)
            )
    )
    public ServerResponse hello(ServerRequest request) {
        HelloWorldResponse response = new HelloWorldResponse("Hello World");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
