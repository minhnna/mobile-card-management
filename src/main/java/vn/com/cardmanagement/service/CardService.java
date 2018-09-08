package vn.com.cardmanagement.service;

import vn.com.cardmanagement.service.dto.CardDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
