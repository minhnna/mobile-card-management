package vn.com.cardmanagement.service.impl;

import vn.com.cardmanagement.service.TopupRequestService;
import vn.com.cardmanagement.domain.TopupRequest;
import vn.com.cardmanagement.repository.TopupRequestRepository;
import vn.com.cardmanagement.service.dto.TopupRequestDTO;
import vn.com.cardmanagement.service.mapper.TopupRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing TopupRequest.
 */
@Service
public class TopupRequestServiceImpl implements TopupRequestService {

    private final Logger log = LoggerFactory.getLogger(TopupRequestServiceImpl.class);

    private final TopupRequestRepository topupRequestRepository;

    private final TopupRequestMapper topupRequestMapper;

    public TopupRequestServiceImpl(TopupRequestRepository topupRequestRepository, TopupRequestMapper topupRequestMapper) {
        this.topupRequestRepository = topupRequestRepository;
        this.topupRequestMapper = topupRequestMapper;
    }

    /**
     * Save a topupRequest.
     *
     * @param topupRequestDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TopupRequestDTO save(TopupRequestDTO topupRequestDTO) {
        log.debug("Request to save TopupRequest : {}", topupRequestDTO);
        TopupRequest topupRequest = topupRequestMapper.toEntity(topupRequestDTO);
        topupRequest = topupRequestRepository.save(topupRequest);
        return topupRequestMapper.toDto(topupRequest);
    }

    /**
     * Get all the topupRequests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<TopupRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TopupRequests");
        return topupRequestRepository.findAll(pageable)
            .map(topupRequestMapper::toDto);
    }


    /**
     * Get one topupRequest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<TopupRequestDTO> findOne(String id) {
        log.debug("Request to get TopupRequest : {}", id);
        return topupRequestRepository.findById(id)
            .map(topupRequestMapper::toDto);
    }

    /**
     * Delete the topupRequest by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete TopupRequest : {}", id);
        topupRequestRepository.deleteById(id);
    }
}
