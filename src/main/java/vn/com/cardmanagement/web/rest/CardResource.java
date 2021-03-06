package vn.com.cardmanagement.web.rest;
import vn.com.cardmanagement.service.CardService;
import vn.com.cardmanagement.web.rest.errors.BadRequestAlertException;
import vn.com.cardmanagement.web.rest.util.HeaderUtil;
import vn.com.cardmanagement.web.rest.util.PaginationUtil;
import vn.com.cardmanagement.service.dto.CardDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Card.
 */
@RestController
@RequestMapping("/api")
public class CardResource {

    private final Logger log = LoggerFactory.getLogger(CardResource.class);

    private static final String ENTITY_NAME = "card";

    private final CardService cardService;

    public CardResource(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * POST  /cards : Create a new card.
     *
     * @param cardDTO the cardDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cardDTO, or with status 400 (Bad Request) if the card has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cards")
    public ResponseEntity<CardDTO> createCard(@RequestBody CardDTO cardDTO) throws URISyntaxException {
        log.debug("REST request to save Card : {}", cardDTO);
        if (cardDTO.getId() != null) {
            throw new BadRequestAlertException("A new card cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardDTO result = cardService.save(cardDTO);
        return ResponseEntity.created(new URI("/api/cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cards : Updates an existing card.
     *
     * @param cardDTO the cardDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cardDTO,
     * or with status 400 (Bad Request) if the cardDTO is not valid,
     * or with status 500 (Internal Server Error) if the cardDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cards")
    public ResponseEntity<CardDTO> updateCard(@RequestBody CardDTO cardDTO) throws URISyntaxException {
        log.debug("REST request to update Card : {}", cardDTO);
        if (cardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CardDTO result = cardService.save(cardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cardDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cards : get all the cards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cards in body
     */
    @GetMapping("/cards")
    public ResponseEntity<List<CardDTO>> getAllCards(Pageable pageable) {
        log.debug("REST request to get a page of Cards");
        Page<CardDTO> page = cardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cards");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /cards/:id : get the "id" card.
     *
     * @param id the id of the cardDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cardDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cards/{id}")
    public ResponseEntity<CardDTO> getCard(@PathVariable String id) {
        log.debug("REST request to get Card : {}", id);
        Optional<CardDTO> cardDTO = cardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardDTO);
    }

    /**
     * DELETE  /cards/:id : delete the "id" card.
     *
     * @param id the id of the cardDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable String id) {
        log.debug("REST request to delete Card : {}", id);
        cardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
