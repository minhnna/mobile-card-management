package vn.com.cardmanagement.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Strings;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import vn.com.cardmanagement.config.Constants;
import vn.com.cardmanagement.service.CardService;
import vn.com.cardmanagement.web.rest.errors.BadRequestAlertException;
import vn.com.cardmanagement.web.rest.params.CardQueryCondition;
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

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

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
    @Timed
    public ResponseEntity<CardDTO> createCard(@RequestBody CardDTO cardDTO) throws URISyntaxException {
        log.debug("REST request to save Card : {}", cardDTO);
        if (cardDTO.getId() != null) {
            throw new BadRequestAlertException("A new card cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cardDTO.setCreatedDate(Instant.now());
        cardDTO.setStatus(String.valueOf(Constants.Status.NEW));
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
    @Timed
    public ResponseEntity<CardDTO> updateCard(@RequestBody CardDTO cardDTO) throws URISyntaxException {
        log.debug("REST request to update Card : {}", cardDTO);
        if (cardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (Strings.isNullOrEmpty(cardDTO.getStatus())) {
            return ResponseEntity.badRequest().build();
        }
        CardDTO result = cardService.updateStatus(cardDTO);
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
    @Timed
    public ResponseEntity<List<CardDTO>> getAllCards(Pageable pageable) {
        log.debug("REST request to get a page of Cards");
        Page<CardDTO> page = cardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/get-card-by-user")
    @Timed
    public ResponseEntity<List<CardDTO>> getCardsByUser(CardQueryCondition cardQueryCondition) {
        log.debug("REST request to get quantity of Cards");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cardQueryCondition.setUserId(userDetails.getUsername());
        List<CardDTO> result = cardService.findNewCards(cardQueryCondition);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-card-by-user-less-than")
    @Timed
    public ResponseEntity<List<CardDTO>> getCardsByUserLessThan(CardQueryCondition cardQueryCondition) {
        log.debug("REST request to get quantity of Cards");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cardQueryCondition.setUserId(userDetails.getUsername());
        List<CardDTO> result = cardService.findNewLessThanCards(cardQueryCondition);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-expired-card")
    @Timed
    public ResponseEntity<List<CardDTO>> getExpiredCard(Pageable pageable) {
        log.debug("REST request to get expired Cards");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<CardDTO> page = cardService.findExpiredCards(pageable, userDetails.getUsername());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/get-expired-card");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/view-card-by-user")
    @Timed
    public ResponseEntity<List<CardDTO>> viewCardsByUser(Pageable pageable, CardQueryCondition cardQueryCondition) {
        log.debug("REST request to view old Cards by user");
        cardQueryCondition.convertDateToMilliseconds();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cardQueryCondition.setUserId(userDetails.getUsername());
        Page<CardDTO> page = cardService.findOldCards(pageable, cardQueryCondition);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/view-card-by-user");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/generate-excel-by-user")
    @Timed
    public ResponseEntity<InputStreamResource> generateExcelByUser(CardQueryCondition cardQueryCondition) throws ParseException {
        log.debug("REST request to generate Excel by user");

        String fromDate = cardQueryCondition.getFromDate();
        String toDate = cardQueryCondition.getToDate();
        String excelFileName = fromDate.concat(toDate).concat(".xlsx");
        cardQueryCondition.convertDateToMilliseconds();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cardQueryCondition.setUserId(userDetails.getUsername());
        File archivo = cardService.exportReportForUser(cardQueryCondition);
        InputStream file = null;
        try {
            file = new FileInputStream(archivo);
        } catch (FileNotFoundException e) {
            log.info("ERROR export file");
            e.printStackTrace();
        }
//        byte[] documentContent = archivo.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
//        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"myexcelfile.xlsx\"");
//        headers.setContentLength(documentContent.length);
        return new ResponseEntity<InputStreamResource>(new InputStreamResource(file), headers, HttpStatus.OK);
    }

    @GetMapping("/generate-excel-by-admin")
    @Timed
    public ResponseEntity<InputStreamResource> generateExcelByAdmin(CardQueryCondition cardQueryCondition) throws ParseException {
        log.debug("REST request to generate Excel by user");
        String fromDate = cardQueryCondition.getFromDate();
        String toDate = cardQueryCondition.getToDate();
        String excelFileName = fromDate.concat(toDate).concat(".xlsx");
        cardQueryCondition.convertDateToMilliseconds();
        File archivo = cardService.exportReportForUser(cardQueryCondition);
        InputStream file = null;
        try {
            file = new FileInputStream(archivo);
        } catch (FileNotFoundException e) {
            log.info("ERROR export file");
            e.printStackTrace();
        }
//        byte[] documentContent = archivo.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
//        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"myexcelfile.xlsx\"");
//        headers.setContentLength(documentContent.length);
        return new ResponseEntity<InputStreamResource>(new InputStreamResource(file), headers, HttpStatus.OK);
    }

    @GetMapping("/view-card-by-admin")
    @Timed
    public ResponseEntity<List<CardDTO>> viewCardsByAdmin(Pageable pageable, CardQueryCondition cardQueryCondition) {
        log.debug("REST request to view old Cards by admin");
        cardQueryCondition.convertDateToMilliseconds();
        Page<CardDTO> page = cardService.findOldCards(pageable, cardQueryCondition);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/create-device")
    @Timed
    public ResponseEntity<String> createDevice() {
        log.debug("REST test app");
        cardService.createDevice();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/test-app")
    @Timed
    public ResponseEntity<String> testApp() {
        log.debug("REST test app");
        cardService.loginByMobifone();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/passcode")
    @Timed
    public ResponseEntity<String> inputPasscode(@RequestParam(value = "passcode", required = true) String passcode) {
        log.debug("REST inputPasscode");
        cardService.inputPasscode(passcode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /cards/:id : get the "id" card.
     *
     * @param id the id of the cardDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cardDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cards/{id}")
    @Timed
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
    @Timed
    public ResponseEntity<Void> deleteCard(@PathVariable String id) {
        log.debug("REST request to delete Card : {}", id);
        cardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
