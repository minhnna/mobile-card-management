package vn.com.cardmanagement.service.mapper;

import vn.com.cardmanagement.domain.*;
import vn.com.cardmanagement.service.dto.CardDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Card and its DTO CardDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CardMapper extends EntityMapper<CardDTO, Card> {


}
