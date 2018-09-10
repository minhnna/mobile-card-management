package vn.com.cardmanagement.service.impl;

import org.springframework.beans.BeanUtils;
import springfox.documentation.swagger2.mappers.ModelMapper;
import vn.com.cardmanagement.service.CardService;
import vn.com.cardmanagement.domain.Card;
import vn.com.cardmanagement.repository.CardRepository;
import vn.com.cardmanagement.service.dto.CardDTO;
import vn.com.cardmanagement.service.mapper.CardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.cardmanagement.web.rest.params.CardQueryCondition;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Card.
 */
@Service
public class CardServiceImpl implements CardService {

    private final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardRepository cardRepository;

    private final CardMapper cardMapper;

    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    /**
     * Save a card.
     *
     * @param cardDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CardDTO save(CardDTO cardDTO) {
        log.debug("Request to save Card : {}", cardDTO);
        Card card = cardMapper.toEntity(cardDTO);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    /**
     * Get all the cards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<CardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cards");
        return cardRepository.findAll(pageable)
            .map(cardMapper::toDto);
    }


    /**
     * Get one card by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<CardDTO> findOne(String id) {
        log.debug("Request to get Card : {}", id);
        return cardRepository.findById(id)
            .map(cardMapper::toDto);
    }

    /**
     * Delete the card by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Card : {}", id);
        cardRepository.deleteById(id);
    }

    @Override
    public List<CardDTO> findNewCards(CardQueryCondition cardQueryCondition) {

        return cardMapper.toDto(cardRepository.findNewCard(cardQueryCondition));
    }

    @Override
    public Page<CardDTO> findOldCards(Pageable pageable, CardQueryCondition cardQueryCondition) {
        return cardRepository.findOldCard(pageable, cardQueryCondition).map(cardMapper::toDto);
    }

    @Override
    public Page<CardDTO> findExpiredCards(Pageable pageable) {
        return cardRepository.findExpiredCard(pageable).map(cardMapper::toDto);
    }

    @Override
    public CardDTO updateStatus(CardDTO cardDTO) {
        log.debug("Request to update Card : {}", cardDTO.getId());
        Card orgCard = cardRepository.findOne(cardDTO.getId());
        if (cardDTO.getRealPrice() != 0) {
            orgCard.setRealPrice(cardDTO.getRealPrice());
        }
        orgCard.setStatus(cardDTO.getStatus());
        orgCard.setUpdatedDate(Instant.now());
        Card result = cardRepository.save(orgCard);
        BeanUtils.copyProperties(result, cardDTO);
        return cardDTO;
    }
}
