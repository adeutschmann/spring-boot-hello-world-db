package ch.adeutschmanndev.helloworlddb.router;

import ch.adeutschmanndev.helloworlddb.resource.GreetingsHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

import static org.springframework.web.servlet.function.RequestPredicates.*;

/**
 * Router configuration for all Greetings API endpoints
 * Provides comprehensive REST API for greeting management
 */
@Configuration
@Tag(name = "Greetings API", description = "Complete CRUD operations and search functionality for greetings")
public class GreetingsRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/greetings",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = "createGreeting",
                            summary = "Create a new greeting",
                            description = "Creates a new greeting with the provided message, sender, and recipient",
                            tags = {"Greetings API"},
                            requestBody = @RequestBody(
                                    description = "Greeting creation request",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ch.adeutschmanndev.helloworlddb.model.dto.CreateGreetingRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Greeting created successfully",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ch.adeutschmanndev.helloworlddb.model.dto.GreetingResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid request data",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ch.adeutschmanndev.helloworlddb.model.dto.ErrorResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/greetings/{id}",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "getGreetingById",
                            summary = "Get greeting by ID",
                            description = "Retrieves a specific greeting by its unique identifier",
                            tags = {"Greetings API"},
                            parameters = {
                                    @Parameter(name = "id", description = "Unique greeting identifier", required = true, in = ParameterIn.PATH)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Greeting found",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ch.adeutschmanndev.helloworlddb.model.dto.GreetingResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Greeting not found",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ch.adeutschmanndev.helloworlddb.model.dto.ErrorResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/greetings",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "getAllGreetings",
                            summary = "Get all greetings",
                            description = "Retrieves all greetings in the system",
                            tags = {"Greetings API"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "List of all greetings",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(type = "array", implementation = ch.adeutschmanndev.helloworlddb.model.dto.GreetingResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/greetings/{id}",
                    method = RequestMethod.PUT,
                    operation = @Operation(
                            operationId = "updateGreeting",
                            summary = "Update greeting",
                            description = "Updates an existing greeting with new data",
                            tags = {"Greetings API"},
                            parameters = {
                                    @Parameter(name = "id", description = "Unique greeting identifier", required = true, in = ParameterIn.PATH)
                            },
                            requestBody = @RequestBody(
                                    description = "Greeting update request",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ch.adeutschmanndev.helloworlddb.model.dto.UpdateGreetingRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Greeting updated successfully",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ch.adeutschmanndev.helloworlddb.model.dto.GreetingResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Greeting not found",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ch.adeutschmanndev.helloworlddb.model.dto.ErrorResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/greetings/{id}",
                    method = RequestMethod.DELETE,
                    operation = @Operation(
                            operationId = "deleteGreeting",
                            summary = "Delete greeting",
                            description = "Deletes a greeting by its unique identifier",
                            tags = {"Greetings API"},
                            parameters = {
                                    @Parameter(name = "id", description = "Unique greeting identifier", required = true, in = ParameterIn.PATH)
                            },
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Greeting deleted successfully"),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Greeting not found",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ch.adeutschmanndev.helloworlddb.model.dto.ErrorResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/greetings/sender/{sender}",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "getGreetingsBySender",
                            summary = "Get greetings by sender",
                            description = "Retrieves all greetings sent by a specific sender",
                            tags = {"Greetings API"},
                            parameters = {
                                    @Parameter(name = "sender", description = "Sender name to filter by", required = true, in = ParameterIn.PATH)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "List of greetings from the specified sender",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(type = "array", implementation = ch.adeutschmanndev.helloworlddb.model.dto.GreetingResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/greetings/recipient/{recipient}",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "getGreetingsByRecipient",
                            summary = "Get greetings by recipient",
                            description = "Retrieves all greetings sent to a specific recipient",
                            tags = {"Greetings API"},
                            parameters = {
                                    @Parameter(name = "recipient", description = "Recipient name to filter by", required = true, in = ParameterIn.PATH)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "List of greetings to the specified recipient",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(type = "array", implementation = ch.adeutschmanndev.helloworlddb.model.dto.GreetingResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/greetings/search",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "searchGreetingsByMessage",
                            summary = "Search greetings by message content",
                            description = "Searches for greetings containing specific text in the message",
                            tags = {"Greetings API"},
                            parameters = {
                                    @Parameter(name = "message", description = "Text to search for in greeting messages", required = true, in = ParameterIn.QUERY)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "List of greetings matching the search criteria",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(type = "array", implementation = ch.adeutschmanndev.helloworlddb.model.dto.GreetingResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/greetings/between",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "getGreetingsBetween",
                            summary = "Get greetings between dates",
                            description = "Retrieves greetings created between two specified dates",
                            tags = {"Greetings API"},
                            parameters = {
                                    @Parameter(name = "startDate", description = "Start date (ISO format)", required = true, in = ParameterIn.QUERY),
                                    @Parameter(name = "endDate", description = "End date (ISO format)", required = true, in = ParameterIn.QUERY)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "List of greetings within the specified date range",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(type = "array", implementation = ch.adeutschmanndev.helloworlddb.model.dto.GreetingResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/greetings/after",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "getGreetingsAfterDate",
                            summary = "Get greetings after date",
                            description = "Retrieves greetings created after a specified date",
                            tags = {"Greetings API"},
                            parameters = {
                                    @Parameter(name = "date", description = "Date after which to retrieve greetings (ISO format)", required = true, in = ParameterIn.QUERY)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "List of greetings created after the specified date",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(type = "array", implementation = ch.adeutschmanndev.helloworlddb.model.dto.GreetingResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/greetings/latest/{sender}",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "getLatestGreetingBySender",
                            summary = "Get latest greeting by sender",
                            description = "Retrieves the most recent greeting from a specific sender",
                            tags = {"Greetings API"},
                            parameters = {
                                    @Parameter(name = "sender", description = "Sender name to get latest greeting from", required = true, in = ParameterIn.PATH)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Latest greeting from the specified sender",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ch.adeutschmanndev.helloworlddb.model.dto.GreetingResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "No greetings found for the specified sender",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ch.adeutschmanndev.helloworlddb.model.dto.ErrorResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/greetings/{id}",
                    method = RequestMethod.HEAD,
                    operation = @Operation(
                            operationId = "checkGreetingExists",
                            summary = "Check if greeting exists",
                            description = "Checks if a greeting with the specified ID exists without returning the content",
                            tags = {"Greetings API"},
                            parameters = {
                                    @Parameter(name = "id", description = "Unique greeting identifier", required = true, in = ParameterIn.PATH)
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Greeting exists"),
                                    @ApiResponse(responseCode = "404", description = "Greeting not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/greetings/count",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "getGreetingsCount",
                            summary = "Get greetings count",
                            description = "Returns the total number of greetings in the system",
                            tags = {"Greetings API"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Total count of greetings",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = ch.adeutschmanndev.helloworlddb.model.dto.GreetingCountResponseDto.class)
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> greetingsRoutes(GreetingsHandler greetingsHandler) {
        // Log initialization of greeting routes
        org.slf4j.LoggerFactory.getLogger(GreetingsRouter.class)
                .info("Initializing Greetings API routes with comprehensive CRUD operations");

        return RouterFunctions
                // Basic CRUD operations
                .route(POST("/api/greetings").and(accept(MediaType.APPLICATION_JSON)),
                        greetingsHandler::createGreeting)
                .andRoute(GET("/api/greetings/{id}").and(accept(MediaType.APPLICATION_JSON)),
                        greetingsHandler::getGreetingById)
                .andRoute(GET("/api/greetings").and(accept(MediaType.APPLICATION_JSON)),
                        greetingsHandler::getAllGreetings)
                .andRoute(PUT("/api/greetings/{id}").and(accept(MediaType.APPLICATION_JSON)),
                        greetingsHandler::updateGreeting)
                .andRoute(DELETE("/api/greetings/{id}"),
                        greetingsHandler::deleteGreeting)

                // Search and filter operations
                .andRoute(GET("/api/greetings/sender/{sender}").and(accept(MediaType.APPLICATION_JSON)),
                        greetingsHandler::getGreetingsBySender)
                .andRoute(GET("/api/greetings/recipient/{recipient}").and(accept(MediaType.APPLICATION_JSON)),
                        greetingsHandler::getGreetingsByRecipient)
                .andRoute(GET("/api/greetings/search").and(accept(MediaType.APPLICATION_JSON)),
                        greetingsHandler::searchGreetingsByMessage)
                .andRoute(GET("/api/greetings/between").and(accept(MediaType.APPLICATION_JSON)),
                        greetingsHandler::getGreetingsBetween)
                .andRoute(GET("/api/greetings/after").and(accept(MediaType.APPLICATION_JSON)),
                        greetingsHandler::getGreetingsAfterDate)
                .andRoute(GET("/api/greetings/latest/{sender}").and(accept(MediaType.APPLICATION_JSON)),
                        greetingsHandler::getLatestGreetingBySender)

                // Utility operations
                .andRoute(HEAD("/api/greetings/{id}"),
                        greetingsHandler::checkGreetingExists)
                .andRoute(GET("/api/greetings/count").and(accept(MediaType.APPLICATION_JSON)),
                        greetingsHandler::getGreetingsCount);
    }
}
