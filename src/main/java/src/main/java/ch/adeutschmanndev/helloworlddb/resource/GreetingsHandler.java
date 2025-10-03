package ch.adeutschmanndev.helloworlddb.resource;

import ch.adeutschmanndev.helloworlddb.converter.GreetingConverter;
import ch.adeutschmanndev.helloworlddb.model.dto.*;
import ch.adeutschmanndev.helloworlddb.model.entity.Greetings;
import ch.adeutschmanndev.helloworlddb.service.GreetingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Handler class for all Greetings API operations
 * Follows clean code principles with single responsibility per method
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GreetingsHandler {

    private final GreetingsService greetingsService;
    private final GreetingConverter greetingConverter;

    /**
     * Create a new greeting
     * POST /api/greetings
     */
    public ServerResponse createGreeting(ServerRequest request) {
        log.info("Processing request to create new greeting");

        try {
            final CreateGreetingRequestDto createRequest = request.body(CreateGreetingRequestDto.class);
            log.debug("Received create request: {}", createRequest);

            final Greetings greeting = greetingConverter.fromCreateRequestDto(createRequest);
            final Greetings savedGreeting = greetingsService.saveGreeting(greeting);
            final GreetingResponseDto response = greetingConverter.toResponseDto(savedGreeting);

            log.info("Successfully created greeting with ID: {}", savedGreeting.getId());

            return ServerResponse.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("Error creating greeting: {}", e.getMessage(), e);
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Failed to create greeting: " + e.getMessage())
                            .build());
        }
    }

    /**
     * Get greeting by ID
     * GET /api/greetings/{id}
     */
    public ServerResponse getGreetingById(ServerRequest request) {
        final String idParam = request.pathVariable("id");
        log.info("Processing request to get greeting by ID: {}", idParam);

        try {
            final UUID id = UUID.fromString(idParam);
            final Optional<Greetings> greeting = greetingsService.findGreetingById(id);

            if (greeting.isPresent()) {
                final GreetingResponseDto response = greetingConverter.toResponseDto(greeting.get());
                log.info("Successfully retrieved greeting with ID: {}", id);

                return ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            } else {
                log.warn("Greeting not found with ID: {}", id);
                return ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(ErrorResponseDto.builder()
                                .message("Greeting not found with ID: " + id)
                                .build());
            }

        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", idParam);
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Invalid UUID format: " + idParam)
                            .build());
        } catch (Exception e) {
            log.error("Error retrieving greeting: {}", e.getMessage(), e);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Internal server error")
                            .build());
        }
    }

    /**
     * Get all greetings
     * GET /api/greetings
     */
    public ServerResponse getAllGreetings(ServerRequest request) {
        log.info("Processing request to get all greetings");

        try {
            final List<Greetings> greetings = greetingsService.findAllGreetings();
            final List<GreetingResponseDto> response = greetingConverter.toResponseDtoList(greetings);

            log.info("Successfully retrieved {} greetings", greetings.size());

            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("Error retrieving all greetings: {}", e.getMessage(), e);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Failed to retrieve greetings")
                            .build());
        }
    }

    /**
     * Update greeting by ID
     * PUT /api/greetings/{id}
     */
    public ServerResponse updateGreeting(ServerRequest request) {
        final String idParam = request.pathVariable("id");
        log.info("Processing request to update greeting with ID: {}", idParam);

        try {
            final UUID id = UUID.fromString(idParam);
            final UpdateGreetingRequestDto updateRequest = request.body(UpdateGreetingRequestDto.class);
            log.debug("Received update request for ID {}: {}", id, updateRequest);

            final Greetings updateData = Greetings.builder()
                    .message(updateRequest.getMessage())
                    .sender(updateRequest.getSender())
                    .recipient(updateRequest.getRecipient())
                    .build();

            final Greetings updatedGreeting = greetingsService.updateGreeting(id, updateData);
            final GreetingResponseDto response = greetingConverter.toResponseDto(updatedGreeting);

            log.info("Successfully updated greeting with ID: {}", id);

            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", idParam);
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Invalid UUID format: " + idParam)
                            .build());
        } catch (RuntimeException e) {
            log.error("Greeting not found for update: {}", e.getMessage());
            return ServerResponse.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            log.error("Error updating greeting: {}", e.getMessage(), e);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Failed to update greeting")
                            .build());
        }
    }

    /**
     * Delete greeting by ID
     * DELETE /api/greetings/{id}
     */
    public ServerResponse deleteGreeting(ServerRequest request) {
        final String idParam = request.pathVariable("id");
        log.info("Processing request to delete greeting with ID: {}", idParam);

        try {
            final UUID id = UUID.fromString(idParam);
            greetingsService.deleteGreeting(id);

            log.info("Successfully deleted greeting with ID: {}", id);

            return ServerResponse.noContent().build();

        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", idParam);
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Invalid UUID format: " + idParam)
                            .build());
        } catch (RuntimeException e) {
            log.error("Greeting not found for deletion: {}", e.getMessage());
            return ServerResponse.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            log.error("Error deleting greeting: {}", e.getMessage(), e);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Failed to delete greeting")
                            .build());
        }
    }

    /**
     * Get greetings by sender
     * GET /api/greetings/sender/{sender}
     */
    public ServerResponse getGreetingsBySender(ServerRequest request) {
        final String sender = request.pathVariable("sender");
        log.info("Processing request to get greetings by sender: {}", sender);

        try {
            final List<Greetings> greetings = greetingsService.findGreetingsBySender(sender);
            final List<GreetingResponseDto> response = greetingConverter.toResponseDtoList(greetings);

            log.info("Successfully retrieved {} greetings for sender: {}", greetings.size(), sender);

            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("Error retrieving greetings by sender: {}", e.getMessage(), e);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Failed to retrieve greetings by sender")
                            .build());
        }
    }

    /**
     * Get greetings by recipient
     * GET /api/greetings/recipient/{recipient}
     */
    public ServerResponse getGreetingsByRecipient(ServerRequest request) {
        final String recipient = request.pathVariable("recipient");
        log.info("Processing request to get greetings by recipient: {}", recipient);

        try {
            final List<Greetings> greetings = greetingsService.findGreetingsByRecipient(recipient);
            final List<GreetingResponseDto> response = greetingConverter.toResponseDtoList(greetings);

            log.info("Successfully retrieved {} greetings for recipient: {}", greetings.size(), recipient);

            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("Error retrieving greetings by recipient: {}", e.getMessage(), e);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Failed to retrieve greetings by recipient")
                            .build());
        }
    }

    /**
     * Search greetings by message content
     * GET /api/greetings/search?message={message}
     */
    public ServerResponse searchGreetingsByMessage(ServerRequest request) {
        final Optional<String> messageParam = request.param("message");

        if (messageParam.isEmpty()) {
            log.warn("Message query parameter is missing");
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Message query parameter is required")
                            .build());
        }

        final String message = messageParam.get();
        log.info("Processing request to search greetings by message: {}", message);

        try {
            final List<Greetings> greetings = greetingsService.findGreetingsByMessage(message);
            final List<GreetingResponseDto> response = greetingConverter.toResponseDtoList(greetings);

            log.info("Successfully found {} greetings containing message: {}", greetings.size(), message);

            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("Error searching greetings by message: {}", e.getMessage(), e);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Failed to search greetings by message")
                            .build());
        }
    }

    /**
     * Get greetings between sender and recipient
     * GET /api/greetings/between?sender={sender}&recipient={recipient}
     */
    public ServerResponse getGreetingsBetween(ServerRequest request) {
        final Optional<String> senderParam = request.param("sender");
        final Optional<String> recipientParam = request.param("recipient");

        if (senderParam.isEmpty() || recipientParam.isEmpty()) {
            log.warn("Missing required query parameters: sender and/or recipient");
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Both sender and recipient query parameters are required")
                            .build());
        }

        final String sender = senderParam.get();
        final String recipient = recipientParam.get();
        log.info("Processing request to get greetings between sender: {} and recipient: {}", sender, recipient);

        try {
            final List<Greetings> greetings = greetingsService.findGreetingsBetween(sender, recipient);
            final List<GreetingResponseDto> response = greetingConverter.toResponseDtoList(greetings);

            log.info("Successfully retrieved {} greetings between {} and {}", greetings.size(), sender, recipient);

            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("Error retrieving greetings between users: {}", e.getMessage(), e);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Failed to retrieve greetings between users")
                            .build());
        }
    }

    /**
     * Get greetings created after specified date
     * GET /api/greetings/after?date={iso-date}
     */
    public ServerResponse getGreetingsAfterDate(ServerRequest request) {
        final Optional<String> dateParam = request.param("date");

        if (dateParam.isEmpty()) {
            log.warn("Date query parameter is missing");
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Date query parameter is required (ISO format)")
                            .build());
        }

        try {
            final LocalDateTime date = LocalDateTime.parse(dateParam.get());
            log.info("Processing request to get greetings after date: {}", date);

            final List<Greetings> greetings = greetingsService.findGreetingsAfterDate(date);
            final List<GreetingResponseDto> response = greetingConverter.toResponseDtoList(greetings);

            log.info("Successfully retrieved {} greetings after date: {}", greetings.size(), date);

            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("Error retrieving greetings after date: {}", e.getMessage(), e);
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Invalid date format. Use ISO format (yyyy-MM-ddTHH:mm:ss)")
                            .build());
        }
    }

    /**
     * Get latest greeting by sender
     * GET /api/greetings/latest/{sender}
     */
    public ServerResponse getLatestGreetingBySender(ServerRequest request) {
        final String sender = request.pathVariable("sender");
        log.info("Processing request to get latest greeting by sender: {}", sender);

        try {
            final Optional<Greetings> greeting = greetingsService.findLatestGreetingBySender(sender);

            if (greeting.isPresent()) {
                final GreetingResponseDto response = greetingConverter.toResponseDto(greeting.get());
                log.info("Successfully retrieved latest greeting for sender: {}", sender);

                return ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            } else {
                log.warn("No greetings found for sender: {}", sender);
                return ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(ErrorResponseDto.builder()
                                .message("No greetings found for sender: " + sender)
                                .build());
            }

        } catch (Exception e) {
            log.error("Error retrieving latest greeting by sender: {}", e.getMessage(), e);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Failed to retrieve latest greeting")
                            .build());
        }
    }

    /**
     * Check if greeting exists by ID
     * HEAD /api/greetings/{id}
     */
    public ServerResponse checkGreetingExists(ServerRequest request) {
        final String idParam = request.pathVariable("id");
        log.info("Processing request to check if greeting exists with ID: {}", idParam);

        try {
            final UUID id = UUID.fromString(idParam);
            final boolean exists = greetingsService.existsById(id);

            log.info("Greeting existence check for ID {}: {}", id, exists);

            if (exists) {
                return ServerResponse.ok().build();
            } else {
                return ServerResponse.notFound().build();
            }

        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", idParam);
            return ServerResponse.badRequest().build();
        } catch (Exception e) {
            log.error("Error checking greeting existence: {}", e.getMessage(), e);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get total count of greetings
     * GET /api/greetings/count
     */
    public ServerResponse getGreetingsCount(ServerRequest request) {
        log.info("Processing request to get total greetings count");

        try {
            final long count = greetingsService.countGreetings();
            final GreetingCountResponseDto response = greetingConverter.toCountResponseDto(count);

            log.info("Successfully retrieved greetings count: {}", count);

            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("Error retrieving greetings count: {}", e.getMessage(), e);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorResponseDto.builder()
                            .message("Failed to retrieve greetings count")
                            .build());
        }
    }
}
