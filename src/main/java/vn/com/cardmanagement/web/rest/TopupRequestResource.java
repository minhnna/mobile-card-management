package vn.com.cardmanagement.web.rest;
import vn.com.cardmanagement.service.TopupRequestService;
import vn.com.cardmanagement.web.rest.errors.BadRequestAlertException;
import vn.com.cardmanagement.web.rest.util.HeaderUtil;
import vn.com.cardmanagement.web.rest.util.PaginationUtil;
import vn.com.cardmanagement.service.dto.TopupRequestDTO;
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
 * REST controller for managing TopupRequest.
 */
@RestController
@RequestMapping("/api")
public class TopupRequestResource {

    private final Logger log = LoggerFactory.getLogger(TopupRequestResource.class);

    private static final String ENTITY_NAME = "topupRequest";

    private final TopupRequestService topupRequestService;

    public TopupRequestResource(TopupRequestService topupRequestService) {
        this.topupRequestService = topupRequestService;
    }

    /**
     * POST  /topup-requests : Create a new topupRequest.
     *
     * @param topupRequestDTO the topupRequestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new topupRequestDTO, or with status 400 (Bad Request) if the topupRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/topup-requests")
    public ResponseEntity<TopupRequestDTO> createTopupRequest(@RequestBody TopupRequestDTO topupRequestDTO) throws URISyntaxException {
        log.debug("REST request to save TopupRequest : {}", topupRequestDTO);
        if (topupRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new topupRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TopupRequestDTO result = topupRequestService.save(topupRequestDTO);
        return ResponseEntity.created(new URI("/api/topup-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /topup-requests : Updates an existing topupRequest.
     *
     * @param topupRequestDTO the topupRequestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated topupRequestDTO,
     * or with status 400 (Bad Request) if the topupRequestDTO is not valid,
     * or with status 500 (Internal Server Error) if the topupRequestDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/topup-requests")
    public ResponseEntity<TopupRequestDTO> updateTopupRequest(@RequestBody TopupRequestDTO topupRequestDTO) throws URISyntaxException {
        log.debug("REST request to update TopupRequest : {}", topupRequestDTO);
        if (topupRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TopupRequestDTO result = topupRequestService.save(topupRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, topupRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /topup-requests : get all the topupRequests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of topupRequests in body
     */
    @GetMapping("/topup-requests")
    public ResponseEntity<List<TopupRequestDTO>> getAllTopupRequests(Pageable pageable) {
        log.debug("REST request to get a page of TopupRequests");
        Page<TopupRequestDTO> page = topupRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/topup-requests");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /topup-requests/:id : get the "id" topupRequest.
     *
     * @param id the id of the topupRequestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the topupRequestDTO, or with status 404 (Not Found)
     */
    @GetMapping("/topup-requests/{id}")
    public ResponseEntity<TopupRequestDTO> getTopupRequest(@PathVariable String id) {
        log.debug("REST request to get TopupRequest : {}", id);
        Optional<TopupRequestDTO> topupRequestDTO = topupRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(topupRequestDTO);
    }

    /**
     * DELETE  /topup-requests/:id : delete the "id" topupRequest.
     *
     * @param id the id of the topupRequestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/topup-requests/{id}")
    public ResponseEntity<Void> deleteTopupRequest(@PathVariable String id) {
        log.debug("REST request to delete TopupRequest : {}", id);
        topupRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
