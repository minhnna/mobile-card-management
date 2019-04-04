package vn.com.cardmanagement.service;

import vn.com.cardmanagement.domain.Card;
import vn.com.cardmanagement.domain.User;
import vn.com.cardmanagement.service.dto.CardDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.cardmanagement.web.rest.params.CardQueryCondition;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Card.
 */
public interface CardService {

    /**
     * Save a card.
     *
     * @param cardDTO the entity to save
     * @return the persisted entity
     */
    CardDTO save(CardDTO cardDTO);

    /**
     * Get all the cards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CardDTO> findAll(Pageable pageable);


    /**
     * Get the "id" card.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CardDTO> findOne(String id);

    /**
     * Delete the "id" card.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    List<CardDTO> findNewCards(CardQueryCondition cardQueryCondition);

    List<CardDTO> findNewLessThanCards(CardQueryCondition cardQueryCondition);

    Page<CardDTO> findOldCards(Pageable pageable, CardQueryCondition cardQueryCondition);

    Page<CardDTO> findExpiredCards(Pageable pageable, String username);

    CardDTO updateStatus(CardDTO cardDTO);

    List<Card> findOldCardsForReport(CardQueryCondition cardQueryCondition);

    File exportReportForUser(CardQueryCondition cardQueryCondition);

    List<User> getAllManagedPendingUsers();

    void testApp() throws MalformedURLException, InterruptedException;
}
