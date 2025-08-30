package ch.adeutschmanndev.helloworld.repository;

import ch.adeutschmanndev.helloworld.model.entity.Greetings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GreetingsRepository extends JpaRepository<Greetings, UUID> {

    // Find greetings by sender
    List<Greetings> findBySender(String sender);

    // Find greetings by recipient
    List<Greetings> findByRecipient(String recipient);

    // Find greetings containing specific message text
    List<Greetings> findByMessageContainingIgnoreCase(String message);

    // Find greetings between sender and recipient
    @Query("SELECT greetings FROM Greetings greetings WHERE greetings.sender = :sender AND greetings.recipient = :recipient")
    List<Greetings> findGreetingsBetween(@Param("sender") String sender, @Param("recipient") String recipient);

    // Find greetings created after a specific date
    List<Greetings> findByCreatedAtAfter(LocalDateTime date);

    // Find latest greeting by sender
    Optional<Greetings> findTopBySenderOrderByCreatedAtDesc(String sender);
}
