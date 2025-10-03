package ch.adeutschmanndev.helloworlddb.service;

import ch.adeutschmanndev.helloworlddb.model.entity.Greetings;
import ch.adeutschmanndev.helloworlddb.repository.GreetingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GreetingsService {

    private final GreetingsRepository greetingsRepository;

    /**
     * Save a new greeting
     */
    public Greetings saveGreeting(Greetings greeting) {
        log.info("Saving new greeting from {} to {}", greeting.getSender(), greeting.getRecipient());
        return greetingsRepository.save(greeting);
    }

    /**
     * Read greeting by ID
     */
    @Transactional(readOnly = true)
    public Optional<Greetings> findGreetingById(UUID id) {
        log.info("Finding greeting with ID: {}", id);
        return greetingsRepository.findById(id);
    }

    /**
     * Read all greetings
     */
    @Transactional(readOnly = true)
    public List<Greetings> findAllGreetings() {
        log.info("Retrieving all greetings");
        return greetingsRepository.findAll();
    }

    /**
     * Update an existing greeting
     */
    public Greetings updateGreeting(UUID id, Greetings updatedGreeting) {
        log.info("Updating greeting with ID: {}", id);
        return greetingsRepository.findById(id)
                .map(existingGreeting -> {
                    existingGreeting.setMessage(updatedGreeting.getMessage());
                    existingGreeting.setSender(updatedGreeting.getSender());
                    existingGreeting.setRecipient(updatedGreeting.getRecipient());
                    return greetingsRepository.save(existingGreeting);
                })
                .orElseThrow(() -> new RuntimeException("Greeting not found with ID: " + id));
    }

    /**
     * Delete greeting by ID
     */
    public void deleteGreeting(UUID id) {
        log.info("Deleting greeting with ID: {}", id);
        if (!greetingsRepository.existsById(id)) {
            throw new RuntimeException("Greeting not found with ID: " + id);
        }
        greetingsRepository.deleteById(id);
    }

    /**
     * Find greetings by sender
     */
    @Transactional(readOnly = true)
    public List<Greetings> findGreetingsBySender(String sender) {
        log.info("Finding greetings by sender: {}", sender);
        return greetingsRepository.findBySender(sender);
    }

    /**
     * Find greetings by recipient
     */
    @Transactional(readOnly = true)
    public List<Greetings> findGreetingsByRecipient(String recipient) {
        log.info("Finding greetings by recipient: {}", recipient);
        return greetingsRepository.findByRecipient(recipient);
    }

    /**
     * Find greetings containing specific message text
     */
    @Transactional(readOnly = true)
    public List<Greetings> findGreetingsByMessage(String message) {
        log.info("Finding greetings containing message: {}", message);
        return greetingsRepository.findByMessageContainingIgnoreCase(message);
    }

    /**
     * Find greetings between sender and recipient
     */
    @Transactional(readOnly = true)
    public List<Greetings> findGreetingsBetween(String sender, String recipient) {
        log.info("Finding greetings between {} and {}", sender, recipient);
        return greetingsRepository.findGreetingsBetween(sender, recipient);
    }

    /**
     * Find greetings created after a specific date
     */
    @Transactional(readOnly = true)
    public List<Greetings> findGreetingsAfterDate(LocalDateTime date) {
        log.info("Finding greetings created after: {}", date);
        return greetingsRepository.findByCreatedAtAfter(date);
    }

    /**
     * Find latest greeting by sender
     */
    @Transactional(readOnly = true)
    public Optional<Greetings> findLatestGreetingBySender(String sender) {
        log.info("Finding latest greeting by sender: {}", sender);
        return greetingsRepository.findTopBySenderOrderByCreatedAtDesc(sender);
    }

    /**
     * Check if greeting exists by ID
     */
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return greetingsRepository.existsById(id);
    }

    /**
     * Count total greetings
     */
    @Transactional(readOnly = true)
    public long countGreetings() {
        return greetingsRepository.count();
    }
}
