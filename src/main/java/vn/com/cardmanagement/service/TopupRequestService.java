package vn.com.cardmanagement.service;

import org.springframework.scheduling.annotation.Scheduled;
import vn.com.cardmanagement.service.dto.TopupRequestDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing TopupRequest.
 */
public interface TopupRequestService {

    /**
     * Save a topupRequest.
     *
     * @param topupRequestDTO the entity to save
     * @return the persisted entity
     */
    TopupRequestDTO save(TopupRequestDTO topupRequestDTO);

    /**
     * Get all the topupRequests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TopupRequestDTO> findAll(Pageable pageable);


    /**
     * Get the "id" topupRequest.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TopupRequestDTO> findOne(String id);

    /**
     * Delete the "id" topupRequest.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    boolean loginByMobifone();

    boolean inputPasscode(String passcode);

    void createDevice();

    @Scheduled(fixedDelay = 15000)
    void runAutoTopup();
}
