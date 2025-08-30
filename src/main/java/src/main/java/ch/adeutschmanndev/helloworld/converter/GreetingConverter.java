package ch.adeutschmanndev.helloworld.converter;

import ch.adeutschmanndev.helloworld.model.dto.CreateGreetingRequestDto;
import ch.adeutschmanndev.helloworld.model.dto.GreetingCountResponseDto;
import ch.adeutschmanndev.helloworld.model.dto.GreetingResponseDto;
import ch.adeutschmanndev.helloworld.model.dto.UpdateGreetingRequestDto;
import ch.adeutschmanndev.helloworld.model.entity.Greetings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converter class for transforming Greetings entities to DTOs
 * Follows clean code principles with clear, single-responsibility methods
 */
@Component
@Slf4j
public class GreetingConverter {

    /**
     * Converts a single Greetings entity to GreetingResponseDto
     *
     * @param greeting The entity to convert
     * @return Converted DTO representation
     */
    public GreetingResponseDto toResponseDto(Greetings greeting) {
        log.debug("Converting Greetings entity to GreetingResponseDto for ID: {}", greeting.getId());

        return GreetingResponseDto.builder()
                .id(greeting.getId())
                .message(greeting.getMessage())
                .sender(greeting.getSender())
                .recipient(greeting.getRecipient())
                .createdAt(greeting.getCreatedAt())
                .updatedAt(greeting.getUpdatedAt())
                .build();
    }

    /**
     * Converts a list of Greetings entities to List of GreetingResponseDto
     *
     * @param greetings The list of entities to convert
     * @return List of converted DTOs
     */
    public List<GreetingResponseDto> toResponseDtoList(List<Greetings> greetings) {
        log.debug("Converting {} Greetings entities to GreetingResponseDto list", greetings.size());

        return greetings.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Converts a count value to GreetingCountResponseDto
     *
     * @param count The count value
     * @return Count response DTO
     */
    public GreetingCountResponseDto toCountResponseDto(long count) {
        log.debug("Converting count {} to GreetingCountResponseDto", count);

        return GreetingCountResponseDto.builder()
                .count(count)
                .build();
    }

    /**
     * Converts CreateGreetingRequestDto to Greetings entity
     *
     * @param createRequest The request DTO
     * @return New Greetings entity
     */
    public Greetings fromCreateRequestDto(CreateGreetingRequestDto createRequest) {
        log.debug("Converting CreateGreetingRequestDto to Greetings entity");

        return Greetings.builder()
                .message(createRequest.getMessage())
                .sender(createRequest.getSender())
                .recipient(createRequest.getRecipient())
                .build();
    }

    /**
     * Updates an existing Greetings entity with data from UpdateGreetingRequestDto
     *
     * @param existingGreeting The existing entity to update
     * @param updateRequest    The update request DTO
     * @return Updated entity
     */
    public Greetings updateFromRequestDto(Greetings existingGreeting,
                                          UpdateGreetingRequestDto updateRequest) {
        log.debug("Updating Greetings entity with ID: {} from UpdateGreetingRequestDto", existingGreeting.getId());

        existingGreeting.setMessage(updateRequest.getMessage());
        existingGreeting.setSender(updateRequest.getSender());
        existingGreeting.setRecipient(updateRequest.getRecipient());

        return existingGreeting;
    }
}
