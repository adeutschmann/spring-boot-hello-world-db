package ch.adeutschmanndev.helloworlddb.router;

import ch.adeutschmanndev.helloworlddb.resource.HelloWorldHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RequestPredicates.accept;

@Configuration
@Tag(name = "Hello World API", description = "Simple greeting API endpoints")
public class HelloWorldRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/hello",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "sayHello",
                            summary = "Get Hello World greeting",
                            description = "Returns a simple Hello World message in JSON format",
                            tags = {"Hello World API"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful response with Hello World message",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(
                                                            type = "object",
                                                            example = "{\"message\": \"Hello World\"}"
                                                    ),
                                                    examples = @ExampleObject(
                                                            name = "Hello World Response",
                                                            summary = "Standard greeting response",
                                                            value = "{\"message\": \"Hello World\"}"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(
                                                            type = "object",
                                                            example = "{\"error\": \"Internal server error\"}"
                                                    )
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> helloWorldRoute(HelloWorldHandler helloWorldHandler) {
        return RouterFunctions
                .route(GET("/api/hello").and(accept(MediaType.APPLICATION_JSON)),
                        helloWorldHandler::hello);
    }
}
